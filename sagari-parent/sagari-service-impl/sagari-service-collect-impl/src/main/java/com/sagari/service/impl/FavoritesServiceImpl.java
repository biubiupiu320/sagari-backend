package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.FavoritesInputDTO;
import com.sagari.service.FavoritesService;
import com.sagari.service.entity.Favorites;
import com.sagari.service.entity.FavoritesVO;
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

/**
 * @author biubiupiu~
 */
@RestController
public class FavoritesServiceImpl extends BaseApiService<JSONObject> implements FavoritesService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FavoritesMapper favoritesMapper;

    @Override
    public BaseResponse<JSONObject> createFavorites(@RequestBody @Valid FavoritesInputDTO favoritesInputDTO,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Favorites favorites = new Favorites();
        BeanUtils.copyProperties(favoritesInputDTO, favorites);
        favorites.setUserId(Integer.valueOf(xxlUser.getUserid()));
        favorites.setDel(false);
        favorites.setCreateTime(System.currentTimeMillis());
        favorites.setCount(0);
        if (favoritesMapper.createFavorites(favorites) > 0) {
            return setResultSuccess("创建收藏夹成功");
        }
        return setResultError("创建收藏夹失败");
    }

    @Override
    public BaseResponse<JSONObject> modifyFavorites(@RequestBody @Valid FavoritesInputDTO favoritesInputDTO,
                                                    BindingResult bindingResult) {
        if (favoritesInputDTO.getId() == null || favoritesInputDTO.getId() <= 0) {
            return setResultError("无效的请求");
        }
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Favorites favorites = new Favorites();
        BeanUtils.copyProperties(favoritesInputDTO, favorites);
        favorites.setUserId(Integer.valueOf(xxlUser.getUserid()));
        if (favoritesMapper.modifyFavorites(favorites) > 0) {
            return setResultSuccess("修改收藏夹成功");
        }
        return setResultError("修改收藏夹失败");
    }

    @Override
    public BaseResponse<JSONObject> deleteFavorites(Integer id) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        if (id == null || id <= 0) {
            return setResultError("无效的请求");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (favoritesMapper.checkPermissions(id, userId) > 0) {
            if (favoritesMapper.deleteFavorites(id, userId) > 0) {
                return setResultSuccess("删除收藏夹成功");
            }
        }
        return setResultError("删除收藏夹失败");
    }

    @Override
    public BaseResponse<JSONObject> getFavorites(Integer targetUserId) {
        if (targetUserId == null || targetUserId <= 0) {
            return setResultError("无效的请求");
        }
        List<FavoritesVO> pubFavorites = favoritesMapper.getPubFavorites(targetUserId);
        JSONObject result = new JSONObject();
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            result.put("favorites", JSON.toJSON(pubFavorites));
            return setResultSuccess(result);
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (!userId.equals(targetUserId)) {
            result.put("favorites", JSON.toJSON(pubFavorites));
            return setResultSuccess(result);
        }
        List<FavoritesVO> priFavorites = favoritesMapper.getPriFavorites(targetUserId);
        pubFavorites.addAll(priFavorites);
        result.put("favorites", JSON.toJSON(pubFavorites));
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getPubFavorites(Integer targetUserId) {
        if (targetUserId == null || targetUserId <= 0) {
            return setResultError("无效的请求");
        }
        List<FavoritesVO> pubFavorites = favoritesMapper.getPubFavorites(targetUserId);
        JSONObject result = new JSONObject();
        result.put("favorites", JSON.toJSON(pubFavorites));
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getPriFavorites(Integer targetUserId) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (!userId.equals(targetUserId)) {
            return setResultError("禁止访问");
        }
        List<FavoritesVO> priFavorites = favoritesMapper.getPriFavorites(targetUserId);
        JSONObject result = new JSONObject();
        result.put("favorites", JSON.toJSON(priFavorites));
        return setResultSuccess(result);
    }
}
