package com.sagari.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.service.InteractiveService;
import com.sagari.service.entity.Interactive;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.feign.CommentServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.InteractArticleMapper;
import com.sagari.service.mapper.InteractCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author biubiupiu~
 */
@RestController
public class InteractiveServiceImpl extends BaseApiService<JSONObject> implements InteractiveService {

    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private ArticleServiceFeign articleServiceFeign;
    @Autowired
    private CommentServiceFeign commentServiceFeign;
    @Autowired
    private InteractArticleMapper interactArticleMapper;
    @Autowired
    private InteractCommentMapper interactCommentMapper;

    /**
     * type为1代表点赞的是文章
     *     2代表点赞的是父评论
     *     3代表点赞的是子评论
     * 下同
     * @param id
     * @param userId
     * @param type
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<JSONObject> toggleGood(Integer targetId, Integer userId, Integer type) {
        if (targetId == null || targetId <= 0 || userId == null || userId <= 0) {
            return setResultError("无效的请求");
        }
        if (!userServiceFeign.isExist(userId)) {
            return setResultError("用户不存在");
        }
        if (type == 1) {
            if (interactArticleMapper.isGood(targetId, userId) > 0) {
                if (interactArticleMapper.delete(targetId, userId) > 0) {
                    articleServiceFeign.decreaseGood(targetId);
                    return setResultSuccess("取消点赞成功");
                }
                return setResultError("取消点赞失败");
            } else {
                Interactive interactive = new Interactive();
                interactive.setTargetId(targetId);
                interactive.setUserId(userId);
                interactive.setCreateTime(System.currentTimeMillis());
                interactive.setUpdateTime(interactive.getCreateTime());
                interactive.setGood(true);
                if (interactArticleMapper.insert(interactive) > 0) {
                    articleServiceFeign.incrementGood(targetId);
                    return setResultSuccess("点赞成功");
                }
                return setResultError("点赞失败");
            }
        } else if (type == 2 || type == 3) {
            Boolean flag = type == 2;
            if (interactCommentMapper.isGood(targetId, userId, flag) > 0) {
                if (interactCommentMapper.delete(targetId, userId, flag) > 0) {
                    commentServiceFeign.decreaseGood(targetId, flag);
                    return setResultSuccess("取消点赞成功");
                }
                return setResultError("取消点赞失败");
            } else {
                Interactive interactive = new Interactive();
                interactive.setTargetId(targetId);
                interactive.setUserId(userId);
                interactive.setCreateTime(System.currentTimeMillis());
                interactive.setUpdateTime(interactive.getCreateTime());
                interactive.setGood(true);
                interactive.setType(flag);
                if (interactCommentMapper.insert(interactive) > 0) {
                    commentServiceFeign.incrementGood(targetId, flag);
                    return setResultSuccess("点赞成功");
                }
                return setResultError("点赞失败");
            }
        } else {
            return setResultError("无效的请求");
        }
    }

    @Override
    public Boolean isGood(Integer targetId, Integer userId, Integer type) {
        if (targetId == null || targetId <= 0 || userId == null || userId <= 0) {
            return false;
        }
        if (!userServiceFeign.isExist(userId)) {
            return false;
        }
        if (type == 1) {
            return interactArticleMapper.isGood(targetId, userId) > 0;
        } else if (type == 2) {
            // true代表检验父评论
            return interactCommentMapper.isGood(targetId, userId, true) > 0;
        } else if (type == 3){
            // false代表检验子评论
            return interactCommentMapper.isGood(targetId, userId, false) > 0;
        } else {
            return false;
        }
    }
}
