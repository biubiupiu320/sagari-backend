package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.NoticeCommentDTO;
import com.sagari.dto.input.NoticeFollowDTO;
import com.sagari.dto.input.NoticeGoodDTO;
import com.sagari.service.NoticeService;
import com.sagari.service.entity.*;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.feign.CommentServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.NoticeCommentMapper;
import com.sagari.service.mapper.NoticeFollowMapper;
import com.sagari.service.mapper.NoticeGoodMapper;
import com.sagari.service.mapper.NoticeSystemMapper;
import com.xxl.sso.core.login.SsoTokenLoginHelper;
import com.xxl.sso.core.user.XxlSsoUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class NoticeServiceImpl extends BaseApiService<JSONObject> implements NoticeService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private NoticeCommentMapper commentMapper;
    @Autowired
    private NoticeFollowMapper followMapper;
    @Autowired
    private NoticeGoodMapper goodMapper;
    @Autowired
    private NoticeSystemMapper systemMapper;
    @Autowired
    private ArticleServiceFeign articleServiceFeign;
    @Autowired
    private CommentServiceFeign commentServiceFeign;
    @Autowired
    private UserServiceFeign userServiceFeign;

    @Override
    public BaseResponse<JSONObject> getNoticeComment(Integer page, Integer size) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (page < 1) {
            page = 1;
        }
        if (size < 10) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<NoticeCommentVO> list = commentMapper.getNotice(userId);
        if (list == null || list.isEmpty()) {
            return setResultSuccess();
        }

        List<Integer> userIds = list.stream()
                .map(NoticeCommentVO::getFromId)
                .distinct()
                .collect(Collectors.toList());
        JSONArray userArray = userServiceFeign.getSimpleUserByList(userIds).getData().getJSONArray("users");
        Map<Integer, String> userMap = userArray.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("username")));

        Map<Integer, List<NoticeCommentVO>> idMap = list.stream().
                collect(Collectors.groupingBy(NoticeCommentVO::getType));

        List<Integer> contentIds1 = new ArrayList<>();
        List<Integer> contentIds2 = new ArrayList<>();

        Map<Integer, String> articleMap = new HashMap<>(16);
        if (idMap.get(1) != null && !idMap.get(1).isEmpty()) {
            List<Integer> articleIds = idMap.get(1).stream()
                    .map(NoticeCommentVO::getTargetId)
                    .collect(Collectors.toList());
            contentIds1.addAll(idMap.get(1).stream()
                    .map(NoticeCommentVO::getContentId)
                    .collect(Collectors.toList()));
            if (!articleIds.isEmpty()) {
                JSONArray articleArray = articleServiceFeign.getTitle(articleIds)
                        .getData().getJSONArray("titles");
                articleMap = articleArray.stream()
                        .map(o -> (JSONObject) JSON.toJSON(o))
                        .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("title")));
            }
        }

        Map<Integer, String> parentMap = new HashMap<>(16);
        if (idMap.get(2) != null && !idMap.get(2).isEmpty()) {
            List<Integer> parentIds = idMap.get(2).stream()
                    .map(NoticeCommentVO::getTargetId)
                    .collect(Collectors.toList());
            contentIds2.addAll(idMap.get(2).stream()
                    .map(NoticeCommentVO::getContentId)
                    .collect(Collectors.toList()));
            if (!parentIds.isEmpty()) {
                JSONArray parentArray = commentServiceFeign.getParentContent(parentIds)
                        .getData().getJSONArray("contents");
                parentMap = parentArray.stream()
                        .map(o -> (JSONObject) JSON.toJSON(o))
                        .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("content")));
            }
        }

        Map<Integer, String> childMap = new HashMap<>(16);
        if (idMap.get(3) != null && !idMap.get(3).isEmpty()) {
            List<Integer> childIds = idMap.get(3).stream()
                    .map(NoticeCommentVO::getTargetId)
                    .collect(Collectors.toList());
            contentIds2.addAll(idMap.get(3).stream()
                    .map(NoticeCommentVO::getContentId)
                    .collect(Collectors.toList()));
            if (!childIds.isEmpty()) {
                JSONArray childArray = commentServiceFeign.getChildContent(childIds)
                        .getData().getJSONArray("contents");
                childMap = childArray.stream()
                        .map(o -> (JSONObject) JSON.toJSON(o))
                        .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("content")));
            }
        }

        JSONArray content1Array = commentServiceFeign.getParentContent(contentIds1)
                .getData().getJSONArray("contents");
        JSONArray content2Array = commentServiceFeign.getChildContent(contentIds2)
                .getData().getJSONArray("contents");
        Map<Integer, String> content1Map = content1Array.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("content")));
        Map<Integer, String> content2Map = content2Array.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("content")));

        for (NoticeCommentVO notice : list) {
            notice.setToUsername(xxlUser.getUsername());
            notice.setFromUsername(userMap.get(notice.getFromId()));
            if (notice.getType().equals(1)) {
                notice.setText(articleMap.get(notice.getTargetId()));
                notice.setContent(content1Map.get(notice.getContentId()));
            } else if (notice.getType().equals(2)) {
                notice.setText(parentMap.get(notice.getTargetId()));
                notice.setContent(content2Map.get(notice.getContentId()));
            } else if (notice.getType().equals(3)) {
                notice.setText(childMap.get(notice.getTargetId()));
                notice.setContent(content2Map.get(notice.getContentId()));
            }
        }

        PageInfo<NoticeCommentVO> pageInfo = new PageInfo<>(list);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }

    @Override
    public BaseResponse<JSONObject> getNoticeFollow(Integer page, Integer size) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (page < 1) {
            page = 1;
        }
        if (size < 10) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<NoticeFollowVO> list = followMapper.getNotice(userId);
        if (list == null || list.isEmpty()) {
            return setResultSuccess();
        }
        List<Integer> userIds = list.stream()
                .map(NoticeFollowVO::getFromId)
                .distinct()
                .collect(Collectors.toList());

        JSONArray userArray = userServiceFeign.getSimpleUserByList(userIds)
                .getData().getJSONArray("users");
        Map<Integer, String> userMap = userArray.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("username")));

        for (NoticeFollowVO notice : list) {
            notice.setToUsername(xxlUser.getUsername());
            notice.setFromUsername(userMap.get(notice.getFromId()));
        }

        PageInfo<NoticeFollowVO> pageInfo = new PageInfo<>(list);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }

    @Override
    public BaseResponse<JSONObject> getNoticeGood(Integer page, Integer size) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (page < 1) {
            page = 1;
        }
        if (size < 10) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<NoticeGoodVO> list = goodMapper.getNotice(userId);
        if (list == null || list.isEmpty()) {
            return setResultSuccess();
        }

        List<Integer> userIds = list.stream()
                .map(NoticeGoodVO::getFromId)
                .distinct()
                .collect(Collectors.toList());
        JSONArray userArray = userServiceFeign.getSimpleUserByList(userIds).getData().getJSONArray("users");
        Map<Integer, String> userMap = userArray.stream()
                .map(o -> (JSONObject) JSON.toJSON(o))
                .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("username")));

        Map<Integer, List<NoticeGoodVO>> idMap = list.stream().
                collect(Collectors.groupingBy(NoticeGoodVO::getType));

        Map<Integer, String> articleMap = new HashMap<>(16);
        if (idMap.get(1) != null && !idMap.get(1).isEmpty()) {
            List<Integer> articleIds = idMap.get(1).stream()
                    .map(NoticeGoodVO::getTargetId)
                    .collect(Collectors.toList());
            if (!articleIds.isEmpty()) {
                JSONArray articleArray = articleServiceFeign.getTitle(articleIds)
                        .getData().getJSONArray("titles");
                articleMap = articleArray.stream()
                        .map(o -> (JSONObject) JSON.toJSON(o))
                        .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("title")));
            }
        }

        Map<Integer, String> parentMap = new HashMap<>(16);
        if (idMap.get(2) != null && !idMap.get(2).isEmpty()) {
            List<Integer> parentIds = idMap.get(2).stream()
                    .map(NoticeGoodVO::getTargetId)
                    .collect(Collectors.toList());
            if (!parentIds.isEmpty()) {
                JSONArray parentArray = commentServiceFeign.getParentContent(parentIds)
                        .getData().getJSONArray("contents");
                parentMap = parentArray.stream()
                        .map(o -> (JSONObject) JSON.toJSON(o))
                        .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("content")));
            }
        }

        Map<Integer, String> childMap = new HashMap<>(16);
        if (idMap.get(3) != null && !idMap.get(3).isEmpty()) {
            List<Integer> childIds = idMap.get(3).stream()
                    .map(NoticeGoodVO::getTargetId)
                    .collect(Collectors.toList());
            if (!childIds.isEmpty()) {
                JSONArray childArray = commentServiceFeign.getChildContent(childIds)
                        .getData().getJSONArray("contents");
                childMap = childArray.stream()
                        .map(o -> (JSONObject) JSON.toJSON(o))
                        .collect(Collectors.toMap(o -> o.getInteger("id"), o -> o.getString("content")));
            }
        }

        for (NoticeGoodVO notice : list) {
            notice.setToUsername(xxlUser.getUsername());
            notice.setFromUsername(userMap.get(notice.getFromId()));
            if (notice.getType().equals(1)) {
                notice.setText(articleMap.get(notice.getTargetId()));
            } else if (notice.getType().equals(2)) {
                notice.setText(parentMap.get(notice.getTargetId()));
            } else if (notice.getType().equals(3)) {
                notice.setText(childMap.get(notice.getTargetId()));
            }
        }

        PageInfo<NoticeGoodVO> pageInfo = new PageInfo<>(list);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }

    @Override
    public Boolean noticeComment(@RequestBody NoticeCommentDTO noticeCommentDTO) {
        NoticeComment noticeComment = new NoticeComment();
        BeanUtils.copyProperties(noticeCommentDTO, noticeComment);
        noticeComment.setCreateTime(System.currentTimeMillis());
        noticeComment.setUpdateTime(noticeComment.getCreateTime());
        noticeComment.setRead(false);
        noticeComment.setDel(false);
        return commentMapper.createNotice(noticeComment) > 0;
    }

    @Override
    public Boolean noticeFollow(@RequestBody NoticeFollowDTO noticeFollowDTO) {
        NoticeFollow noticeFollow = new NoticeFollow();
        BeanUtils.copyProperties(noticeFollowDTO, noticeFollow);
        noticeFollow.setCreateTime(System.currentTimeMillis());
        noticeFollow.setUpdateTime(noticeFollow.getCreateTime());
        noticeFollow.setRead(false);
        noticeFollow.setDel(false);
        return followMapper.createNotice(noticeFollow) > 0;
    }

    @Override
    public Boolean noticeGood(@RequestBody NoticeGoodDTO noticeGoodDTO) {
        NoticeGood noticeGood = new NoticeGood();
        BeanUtils.copyProperties(noticeGoodDTO, noticeGood);
        noticeGood.setCreateTime(System.currentTimeMillis());
        noticeGood.setUpdateTime(noticeGood.getCreateTime());
        noticeGood.setRead(false);
        noticeGood.setDel(false);
        return goodMapper.createNotice(noticeGood) > 0;
    }

    @Override
    public BaseResponse<JSONObject> markReadComment(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (commentMapper.markReadNotice(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> markReadFollow(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (followMapper.markReadNotice(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> markReadGood(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (goodMapper.markReadNotice(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> delNoticeComment(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (commentMapper.deleteNotice(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> delNoticeFollow(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (followMapper.deleteNotice(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> delNoticeGood(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (goodMapper.deleteNotice(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> markReadCommentAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (commentMapper.markReadNoticeAll(userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> markReadFollowAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (followMapper.markReadNoticeAll(userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> markReadGoodAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (goodMapper.markReadNoticeAll(userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> delNoticeCommentAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (commentMapper.deleteNoticeAll(userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> delFollowCommentAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (followMapper.deleteNoticeAll(userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> delGoodCommentAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (goodMapper.deleteNoticeAll(userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> getNoticeSystem(Integer page, Integer size) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (page < 0) {
            page = 1;
        }
        if (size < 10) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        List<NoticeSystemVO> list = systemMapper.getNoticeSystem(userId);
        if (list == null || list.isEmpty()) {
            return setResultSuccess();
        }

        List<Integer> ids = list.stream().map(NoticeSystemVO::getId).collect(Collectors.toList());
        Set<Integer> readSet = new HashSet<>(systemMapper.getRead(ids, userId));

        for (NoticeSystemVO notice : list) {
            notice.setRead(readSet.contains(notice.getId()));
        }

        PageInfo<NoticeSystemVO> pageInfo = new PageInfo<>(list);
        return setResultSuccess((JSONObject) JSON.toJSON(pageInfo));
    }

    @Override
    public BaseResponse<JSONObject> markReadSystem(@RequestBody List<Integer> ids) {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        if (systemMapper.markReadSystem(ids, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> markReadSystemAll() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        List<Integer> unreadIds = systemMapper.getAllId(userId);
        if (systemMapper.markReadSystem(unreadIds, userId, System.currentTimeMillis()) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> createNoticeSystem(String title, String content) {
        NoticeSystem noticeSystem = new NoticeSystem();
        noticeSystem.setTitle(title);
        noticeSystem.setContent(content);
        noticeSystem.setCreateTime(System.currentTimeMillis());
        if (systemMapper.createNoticeSystem(noticeSystem) > 0) {
            return setResultSuccess();
        }
        return setResultError("failed");
    }

    @Override
    public BaseResponse<JSONObject> getUnreadCount() {
        String sessionId = request.getHeader("xxl-sso-session-id");
        XxlSsoUser xxlUser = SsoTokenLoginHelper.loginCheck(sessionId);
        if (xxlUser == null) {
            return setResultError("用户未登录");
        }
        Integer userId = Integer.valueOf(xxlUser.getUserid());
        JSONObject result = new JSONObject();
        Integer commentCount = commentMapper.unreadNoticeCount(userId);
        result.put("unreadCommentCount", commentCount);
        Integer followCount = followMapper.unreadNoticeCount(userId);
        result.put("unreadFollowCount", followCount);
        Integer goodCount = goodMapper.unreadNoticeCount(userId);
        result.put("unreadGoodCount", goodCount);
        Integer systemCount = systemMapper.unreadNoticeCount(userId);
        result.put("unreadSystemCount", systemCount);
        return setResultSuccess(result);
    }
}
