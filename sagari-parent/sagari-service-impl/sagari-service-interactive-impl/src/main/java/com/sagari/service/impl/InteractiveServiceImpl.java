package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.FansIds;
import com.sagari.dto.input.FollowIds;
import com.sagari.dto.input.NoticeFollowDTO;
import com.sagari.dto.input.NoticeGoodDTO;
import com.sagari.service.InteractiveService;
import com.sagari.service.entity.Follow;
import com.sagari.service.entity.Interactive;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.feign.CommentServiceFeign;
import com.sagari.service.feign.NoticeServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.FollowMapper;
import com.sagari.service.mapper.InteractArticleMapper;
import com.sagari.service.mapper.InteractCommentMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private NoticeServiceFeign noticeServiceFeign;
    @Autowired
    private InteractArticleMapper interactArticleMapper;
    @Autowired
    private InteractCommentMapper interactCommentMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private HttpServletRequest request;

    /**
     * type为1代表点赞的是文章
     *     2代表点赞的是父评论
     *     3代表点赞的是子评论
     * 下同
     * @param targetId  //若type为1，则为文章ID，若type为2，则为父评论ID，若type为3，则为子评论ID
     * @param userId    //当前登录的用户ID，将来将替换为targetID对应的用户ID
     * @param type
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<JSONObject> toggleGood(Integer targetId, Integer author,
                                               Integer articleId, Integer type) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (targetId == null || targetId <= 0 || userId <= 0) {
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
                    if (!userId.equals(author)) {
                        NoticeGoodDTO noticeGoodDTO = new NoticeGoodDTO();
                        noticeGoodDTO.setType(type);
                        noticeGoodDTO.setFromId(userId);
                        noticeGoodDTO.setToId(author);
                        noticeGoodDTO.setTargetId(targetId);
                        noticeGoodDTO.setArticleId(articleId);
                        noticeServiceFeign.noticeGood(noticeGoodDTO);
                    }
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
                    if (!userId.equals(author)) {
                        NoticeGoodDTO noticeGoodDTO = new NoticeGoodDTO();
                        noticeGoodDTO.setType(type);
                        noticeGoodDTO.setFromId(userId);
                        noticeGoodDTO.setToId(author);
                        noticeGoodDTO.setTargetId(targetId);
                        noticeGoodDTO.setArticleId(articleId);
                        noticeServiceFeign.noticeGood(noticeGoodDTO);
                    }
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

    @Override
    public BaseResponse<JSONObject> follow(Integer followId) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (userId.equals(followId)) {
            return setResultError("您不能关注您自己");
        }
        Follow follow = new Follow();
        follow.setFollowId(followId);
        follow.setFansId(userId);
        follow.setCreateTime(System.currentTimeMillis());
        follow.setUpdateTime(follow.getCreateTime());
        follow.setDel(false);
        if (followMapper.follow(follow) > 0) {
            //增加用户的关注数量
            userServiceFeign.incrementFollowCount(userId);
            //增加用户的粉丝数量
            userServiceFeign.incrementFansCount(followId);
            NoticeFollowDTO noticeFollowDTO = new NoticeFollowDTO();
            noticeFollowDTO.setFromId(userId);
            noticeFollowDTO.setToId(followId);
            noticeServiceFeign.noticeFollow(noticeFollowDTO);
            return setResultSuccess("关注成功");
        }
        return setResultError("关注失败");
    }

    @Override
    public BaseResponse<JSONObject> cancelFollow(@RequestBody FollowIds follows) {
        List<Integer> followIds = follows.getFollowIds();
        if (followIds == null || followIds.isEmpty()) {
            return setResultSuccess();
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (followMapper.cancelFollow(followIds, userId, System.currentTimeMillis()) > 0) {
            //减少用户的关注数量
            userServiceFeign.decreaseFollowCountN(userId, followIds.size());
            //减少用户的粉丝数量
            userServiceFeign.decreaseFansCountBatch(followIds);
            return setResultSuccess("取消关注成功");
        }
        return setResultError("取消关注失败");
    }

    @Override
    public Boolean isFollow(Integer followId) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return false;
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        return followMapper.isFollow(followId, userId) > 0;
    }

    @Override
    public BaseResponse<JSONObject> removeFans(@RequestBody FansIds fanss) {
        List<Integer> fansIds = fanss.getFansIds();
        if (fansIds == null || fansIds.isEmpty()) {
            return setResultSuccess();
        }
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (followMapper.removeFans(userId, fansIds, System.currentTimeMillis()) > 0) {
            //减少用户的粉丝数量
            userServiceFeign.decreaseFansCountN(userId, fansIds.size());
            //减少用户的关注数量
            userServiceFeign.decreaseFollowCountBatch(fansIds);
            return setResultSuccess("移除粉丝成功");
        }
        return setResultError("移除粉丝失败");
    }

    @Override
    public BaseResponse<JSONObject> getFollowList(Integer fansId, Integer page, Integer size) {
        if (fansId == null || fansId <= 0) {
            String sessionId = request.getHeader("xxl-sso-session-id");
            XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
            if (xxlUser == null) {
                return setResultError("用户未登录");
            }
            fansId = Integer.valueOf(xxlUser.getUserid());
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 10) {
            size = 10;
        }
        //1.获取关注的用户的ID列表
        PageHelper.startPage(page, size);
        List<Integer> followIds = followMapper.getFollowList(fansId);
        if (followIds == null || followIds.isEmpty()) {
            return setResultSuccess();
        }
        //2.根据第一步获得的ID列表查询用户信息
        JSONArray userArray = userServiceFeign.getSimpleUserByList(followIds).getData().getJSONArray("users");
        JSONObject result = (JSONObject) JSON.toJSON(new PageInfo<>(followIds));
        result.remove("list");
        result.put("follows", userArray);
        return setResultSuccess(result);
    }

    @Override
    public BaseResponse<JSONObject> getFansList(Integer followId, Integer page, Integer size) {
        if (followId == null || followId <= 0) {
            String sessionId = request.getHeader("xxl-sso-session-id");
            XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
            if (xxlUser == null) {
                return setResultError("用户未登录");
            }
            followId = Integer.valueOf(xxlUser.getUserid());
        }
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 10) {
            size = 10;
        }
        //1.获取粉丝ID列表
        PageHelper.startPage(page, size);
        List<Integer> fansIds = followMapper.getFansList(followId);
        if (fansIds == null || fansIds.isEmpty()) {
            return setResultSuccess();
        }
        //2.根据第一步获得的ID列表查询用户信息
        JSONObject userObject = userServiceFeign.getSimpleUserByList(fansIds).getData();
        JSONArray userArray = userObject.getJSONArray("users");
        //3.判断某一个粉丝是否关注了自己
        List<Integer> followIds = followMapper.isFollowList(followId, fansIds);
        for (int i = 0; i < userArray.size(); i++) {
            JSONObject user = userArray.getJSONObject(i);
            if (followIds.contains(user.getInteger("id"))) {
                user.put("follow", true);
            } else {
                user.put("follow", false);
            }
        }
        JSONObject result = (JSONObject) JSON.toJSON(new PageInfo<>(fansIds));
        result.remove("list");
        result.put("fans", userArray);
        return setResultSuccess(result);
    }
}
