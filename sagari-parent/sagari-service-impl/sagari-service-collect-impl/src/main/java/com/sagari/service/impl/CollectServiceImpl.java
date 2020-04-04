package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.CollectBatchInputDTO;
import com.sagari.dto.input.CollectInputDTO;
import com.sagari.dto.input.MoveBatchInputDTO;
import com.sagari.service.CollectService;
import com.sagari.service.entity.Collect;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.mapper.CollectMapper;
import com.sagari.service.mapper.FavoritesMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class CollectServiceImpl extends BaseApiService<JSONObject> implements CollectService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private FavoritesMapper favoritesMapper;
    @Autowired
    private ArticleServiceFeign articleServiceFeign;

    @Override
    public BaseResponse<JSONObject> collectArticle(@RequestBody @Valid CollectInputDTO collectInputDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        Collect collect = new Collect();
        BeanUtils.copyProperties(collectInputDTO, collect);
        collect.setUserId(userId);
        collect.setCreateTime(System.currentTimeMillis());
        collect.setUpdateTime(collect.getCreateTime());
        collect.setDel(false);
        if (collectMapper.collectArticle(collect) > 0) {
            favoritesMapper.incrementCount(collectInputDTO.getFavoritesId());
            articleServiceFeign.incrementCollect(collectInputDTO.getArticleId());
            return setResultSuccess("收藏成功");
        }
        return setResultError("收藏失败");
    }

    @Override
    public BaseResponse<JSONObject> cancelCollectArticle(@RequestBody @Valid CollectBatchInputDTO collectBatchInputDTO,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        Integer favoritesId = collectBatchInputDTO.getFavoritesId();
        if (favoritesMapper.checkPermissions(favoritesId, userId) > 0) {
            List<Integer> articleIds = collectBatchInputDTO.getArticleIds();
            if (collectMapper.cancelCollectArticle(favoritesId, articleIds) > 0) {
                articleServiceFeign.decreaseCollectN(articleIds);
                favoritesMapper.decreaseCountN(favoritesId, articleIds.size());
                return setResultSuccess("取消收藏成功");
            }
            return setResultError("取消收藏失败");
        }
        return setResultError("您无权执行此次操作");
    }

    @Override
    public BaseResponse<JSONObject> moveToFavorites(@RequestBody @Valid MoveBatchInputDTO moveBatchInputDTO,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        Integer source = moveBatchInputDTO.getSource();
        Integer target = moveBatchInputDTO.getTarget();
        if (favoritesMapper.checkPermissions(source, userId) > 0 &&
            favoritesMapper.checkPermissions(target, userId) > 0) {
            List<Integer> articleIds = moveBatchInputDTO.getArticleIds();
            if (collectMapper.moveToFavorites(source, target, articleIds) > 0) {
                favoritesMapper.decreaseCountN(source, articleIds.size());
                favoritesMapper.incrementCountN(target, articleIds.size());
                return setResultSuccess("移动至其他收藏夹成功");
            }
            return setResultError("移动至其他收藏夹失败");
        }
        return setResultError("您无权执行此次操作");
    }

    @Override
    public BaseResponse<JSONObject> getCollect(Integer favoritesId, Integer page, Integer size) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 10) {
            size = 10;
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            if (favoritesMapper.isPub(favoritesId) > 0) {
                return getCollectCommon(favoritesId, page, size);
            }
            return setResultError("您无权查看该内容");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (favoritesMapper.checkPermissions(favoritesId, userId) > 0) {
            return getCollectCommon(favoritesId, page, size);
        }
        return setResultError("您无权查看该内容");
    }

    @Override
    public Boolean isCollect(Integer articleId, Integer userId) {
        if (articleId == null || articleId <= 0) {
            return false;
        }
        if (userId == null || userId <= 0) {
            String sessionId = request.getHeader("xxl-sso-session-id");
            XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
            if (xxlUser == null) {
                return false;
            }
            userId = Integer.valueOf(xxlUser.getUserid());
        }
        return collectMapper.isCollect(articleId, userId) > 0;
    }

    private BaseResponse<JSONObject> getCollectCommon(Integer favoritesId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Collect> collects = collectMapper.getCollect(favoritesId);
        List<Integer> articleIds = collects.stream().map(Collect::getArticleId).collect(Collectors.toList());
        JSONObject articleJson = articleServiceFeign.selectArticleList(articleIds).getData();
        JSONArray articleArray = articleJson.getJSONArray("articles");
        PageInfo<Collect> collectPageInfo = new PageInfo<>(collects);
        JSONObject result = (JSONObject) JSON.toJSON(collectPageInfo);
        result.remove("list");
        result.put("articles", articleArray);
        return setResultSuccess(result);
    }
}
