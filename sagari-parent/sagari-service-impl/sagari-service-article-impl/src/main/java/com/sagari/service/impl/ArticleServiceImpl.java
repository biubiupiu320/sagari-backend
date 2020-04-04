package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ArticleInputDTO;
import com.sagari.service.ArticleService;
import com.sagari.service.entity.Article;
import com.sagari.service.entity.ArticleVO;
import com.sagari.service.feign.CollectServiceFeign;
import com.sagari.service.feign.InteractiveServiceFeign;
import com.sagari.service.feign.TagServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.ArticleMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class ArticleServiceImpl extends BaseApiService<JSONObject> implements ArticleService {

    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private InteractiveServiceFeign interactiveServiceFeign;
    @Autowired
    private CollectServiceFeign collectServiceFeign;
    @Autowired
    private TagServiceFeign tagServiceFeign;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public BaseResponse<JSONObject> publishArticle(@RequestBody @Valid ArticleInputDTO articleInputDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String[] tags = articleInputDTO.getTags().split(",");
        List<Integer> tagIds = new ArrayList<>(tags.length);
        for (String tag : tags) {
            try {
                tagIds.add(Integer.parseInt(tag));
            } catch (NumberFormatException e) {
                return setResultError("提交的数据格式不正确");
            }
        }
        Set<String> set = Arrays.stream(tags).collect(Collectors.toSet());
        if (set.size() > 5) {
            return setResultError("文章不同的标签最多只能有5个");
        }
        if (!userServiceFeign.isExist(articleInputDTO.getAuthor())) {
            return setResultError("用户不存在");
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleInputDTO, article, "id");
        article.setCreateTime(System.currentTimeMillis());
        article.setUpdateTime(article.getCreateTime());
        article.setCommentCount(0);
        article.setViewCount(0);
        article.setGoodCount(0);
        article.setCollectCount(0);
        article.setDel(false);
        if (articleMapper.publishArticle(article) > 0) {
            userServiceFeign.incrementArticleCount(article.getAuthor());
            tagServiceFeign.incrArticleCount(tagIds);
            return setResultSuccess("文章发布成功");
        }
        return setResultError("文章发布失败");
    }

    @Override
    public BaseResponse<JSONObject> selectArticle(Integer articleId) {
        if (articleId == null || articleId <= 0) {
            return setResultError("无效的请求");
        }
        ArticleVO articleVO = articleMapper.selectArticle(articleId);
        if (articleVO == null) {
            return setResultError("文章不存在或已删除");
        }
        JSONObject result = (JSONObject) JSON.toJSON(articleVO);
        JSONObject user = userServiceFeign.getSimpleUser(articleVO.getAuthor()).getData();
        result.put("user", user);
        //1.查看该用户有没有点赞
        if (interactiveServiceFeign.isGood(articleVO.getId(), articleVO.getAuthor(), 1)) {
            result.put("good", true);
        } else {
            result.put("good", false);
        }
        //2.查看该用户有没有收藏
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        Integer userId = null;
        if (xxlUser != null) {
            userId = Integer.valueOf(xxlUser.getUserid());
        }
        if (collectServiceFeign.isCollect(articleVO.getId(), userId)) {
            result.put("collect", true);
        } else {
            result.put("collect", false);
        }
        String tag = articleVO.getTags();
        List<Integer> tagList = Arrays.stream(tag.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        JSONArray tags = tagServiceFeign.getTagBatch(tagList).getData().getJSONArray("tags");
        result.remove("tags");
        result.put("tags", tags);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> selectArticleList(List<Integer> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("articles", null);
            return setResultSuccess(jsonObject);
        }
        List<Article> articles = articleMapper.selectArticleList(articleIds);
        List<Integer> authorIds = articles.stream()
                .map(Article::getAuthor)
                .distinct()
                .collect(Collectors.toList());
        JSONObject userJSON = userServiceFeign.getSimpleUserByList(authorIds).getData();
        JSONArray users = userJSON.getJSONArray("users");
        Map<Integer, JSONObject> userMap = users.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o));
        for (int i = 0; i < users.size(); i++) {
            JSONObject user = users.getJSONObject(i);
            userMap.put(user.getInteger("id"), user);
        }
        JSONArray articleArray = (JSONArray) JSON.toJSON(articles);
        for (int i = 0; i < articleArray.size(); i++) {
            JSONObject article = articleArray.getJSONObject(i);
            article.remove("tags");
            article.put("user", userMap.get(article.getInteger("author")));
        }
        JSONObject result = new JSONObject();
        result.put("articles", articleArray);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> editArticle(Integer articleId,
                                                @RequestBody ArticleInputDTO articleInputDTO) {
        if (articleId == null || articleId <= 0) {
            return setResultError("无效的请求");
        }
        JSONObject result = selectArticle(articleId).getData();
        if (result == null) {
            return setResultError("文章不存在或已删除");
        }
        Article source = result.toJavaObject(Article.class);
        if (!source.getAuthor().equals(articleInputDTO.getAuthor())) {
            return setResultError("您不能修改他人的文章");
        }
        String[] tags = articleInputDTO.getTags().split(",");
        Set<String> set = Arrays.stream(tags).collect(Collectors.toSet());
        if (set.size() > 5) {
            return setResultError("文章不同的标签最多只能有5个");
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleInputDTO, article);
        article.setUpdateTime(System.currentTimeMillis());
        if (articleMapper.updateArticle(article) > 0) {
            return setResultSuccess("文章修改成功");
        }
        return setResultError("文章修改失败，原文似乎已经被删除");
    }

    @Override
    public BaseResponse<JSONObject> deleteArticle(Integer articleId) {
        if (articleId == null || articleId <= 0) {
            return setResultError("无效的请求");
        }
        if (articleMapper.deleteArticle(articleId) > 0) {
            return setResultSuccess("文章删除成功");
        }
        return setResultError("文章删除失败");
    }

    @Override
    public BaseResponse<JSONObject> checkPermissions(Integer articleId, Integer creator) {
        if (articleId == null || articleId < 0 ||
            creator == null || creator < 0) {
            return setResultError("无效的请求");
        }
        JSONObject result = new JSONObject();
        if (articleMapper.checkPermissions(articleId, creator) > 0) {
            result.put("isPass", true);
        } else {
            result.put("isPass", false);
        }
        return setResultSuccess(result);
    }

    @Override
    public Boolean isExist(Integer articleId) {
        return articleMapper.isExist(articleId) > 0;
    }

    @Override
    public Boolean incrementGood(Integer articleId) {
        return articleMapper.incrementGood(articleId) > 0;
    }

    @Override
    public Boolean decreaseGood(Integer articleId) {
        return articleMapper.decreaseGood(articleId) > 0;
    }

    @Override
    public Integer getAuthor(Integer articleId) {
        return articleMapper.getAuthor(articleId);
    }

    @Override
    public Boolean incrementComment(Integer articleId) {
        return articleMapper.incrementComment(articleId) > 0;
    }

    @Override
    public Boolean decreaseComment(Integer articleId) {
        return articleMapper.decreaseComment(articleId) > 0;
    }

    @Override
    public Boolean incrementView(Integer articleId) {
        return articleMapper.incrementView(articleId) > 0;
    }

    @Override
    public Boolean incrementCollect(Integer articleId) {
        return articleMapper.incrementCollect(articleId) > 0;
    }

    @Override
    public Boolean decreaseCollect(Integer articleId) {
        return articleMapper.decreaseCollect(articleId) > 0;
    }

    @Override
    public Boolean incrementCollectN(List<Integer> ids) {
        return articleMapper.incrementCollectN(ids) > 0;
    }

    @Override
    public Boolean decreaseCollectN(List<Integer> ids) {
        return articleMapper.decreaseCollectN(ids) > 0;
    }
}
