package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ArticleInputDTO;
import com.sagari.service.ArticleService;
import com.sagari.service.entity.Article;
import com.sagari.service.entity.ArticleVo;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.ArticleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class ArticleServiceImpl extends BaseApiService<JSONObject> implements ArticleService {

    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public BaseResponse<JSONObject> publishArticle(@RequestBody @Valid ArticleInputDTO articleInputDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        String[] tags = articleInputDTO.getTags().split(",");
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
        article.setIsDel(false);
        if (articleMapper.publishArticle(article) > 0) {
            userServiceFeign.incrementArticleCount(article.getAuthor());
            return setResultSuccess("文章发布成功");
        }
        return setResultError("文章发布失败");
    }

    @Override
    public BaseResponse<JSONObject> selectArticle(Integer articleId) {
        if (articleId == null || articleId <= 0) {
            return setResultError("无效的请求");
        }
        ArticleVo articleVo = articleMapper.selectArticle(articleId);
        if (articleVo == null) {
            return setResultError("文章不存在或已删除");
        }
        return setResultSuccess((JSONObject) JSON.toJSON(articleVo));
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
}
