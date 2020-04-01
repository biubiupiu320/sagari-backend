package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ChildCommentDTO;
import com.sagari.dto.input.ParentCommentDTO;
import com.sagari.service.CommentService;
import com.sagari.service.entity.ChildComment;
import com.sagari.service.entity.ChildCommentVo;
import com.sagari.service.entity.ParentComment;
import com.sagari.service.entity.ParentCommentVo;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.ChildCommentMapper;
import com.sagari.service.mapper.ParentCommentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class CommentServiceImpl extends BaseApiService<JSONObject> implements CommentService {

    @Autowired
    private ParentCommentMapper parentCommentMapper;
    @Autowired
    private ChildCommentMapper childCommentMapper;
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private ArticleServiceFeign articleServiceFeign;

    @Override
    public BaseResponse<JSONObject> insertParentComment(@RequestBody @Valid ParentCommentDTO parentCommentDTO,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        Integer author = articleServiceFeign.getAuthor(parentCommentDTO.getArticleId());
        if (author == null) {
            return setResultError("文章不存在或已删除");
        }
        if (!userServiceFeign.isExist(parentCommentDTO.getUserId())) {
            return setResultError("用户不存在");
        }
        ParentComment parentComment = new ParentComment();
        BeanUtils.copyProperties(parentCommentDTO, parentComment);
        parentComment.setAuthorId(author);
        parentComment.setCommentCount(0);
        parentComment.setGoodCount(0);
        parentComment.setIsDel(false);
        parentComment.setCreateTime(System.currentTimeMillis());
        if (parentCommentMapper.insertComment(parentComment) > 0) {
            articleServiceFeign.incrementComment(parentComment.getArticleId());
            return setResultSuccess("评论成功", (JSONObject) JSON.toJSON(parentComment));
        }
        return setResultError("评论失败");
    }

    @Override
    public BaseResponse<JSONObject> deleteParentComment(Integer id, Integer userId, Integer articleId) {
        if (id == null || id <= 0 || userId == null || userId <= 0) {
            return setResultError("无效的请求");
        }
        if (parentCommentMapper.checkPermissions(id, userId) > 0) {
            if (parentCommentMapper.deleteComment(id) > 0) {
                articleServiceFeign.decreaseComment(articleId);
                return setResultSuccess("删除评论成功");
            }
            return setResultError("删除评论失败，评论不存在或已删除");
        } else {
            return setResultError("您不能删除他人的评论");
        }
    }

    @Override
    public BaseResponse<JSONObject> getComment(Integer articleId, Integer userId, Integer page, Integer size) {
        if (articleId == null || articleId <= 0) {
            return setResultError("无效的请求");
        }
        if (page <= 0) {
            page = 1;
        }
        if (size < 5) {
            size = 5;
        }
        if (!articleServiceFeign.isExist(articleId)) {
            return setResultError("文章不存在或已删除");
        }
        PageHelper.startPage(page, size);
        List<ParentCommentVo> list = parentCommentMapper.selectParentComment(articleId, userId);
        PageInfo<ParentCommentVo> parentCommentVo = new PageInfo<>(list);
        JSONObject comments = (JSONObject) JSON.toJSON(parentCommentVo);
        JSONArray result = comments.getJSONArray("list");
        Set<Integer> userIdSet = list.stream().map(ParentCommentVo::getUserId).collect(Collectors.toSet());

        for (int i = 0; i < result.size(); i++) {
            JSONObject object = result.getJSONObject(i);
            Integer parentId = object.getInteger("id");
            JSONObject childObject = selectChildComment(parentId, userId, 0, 2).getData();
            Integer childOffset = childObject.getInteger("offset");
            Integer childTotal = childObject.getInteger("total");
            JSONArray childs = childObject.getJSONArray("child");
            object.put("child", childs);
            object.put("childOffset", childOffset);
            object.put("childTotal", childTotal);
            for (int j = 0; j < childs.size(); j++) {
                userIdSet.add(childs.getJSONObject(j).getInteger("fromId"));
                userIdSet.add(childs.getJSONObject(j).getInteger("toId"));
            }
        }

        List<Integer> userIdList = new ArrayList<>(userIdSet);
        JSONArray userArray = userServiceFeign.getSimpleUserByList(userIdList).getData().getJSONArray("users");
        Map<Integer, JSONObject> userMap = new HashMap<>(16);
        for (int i = 0; i < userArray.size(); i++) {
            JSONObject user = userArray.getJSONObject(i);
            userMap.put(user.getInteger("id"), user);
        }

        for (int i = 0; i < result.size(); i++) {
            JSONObject object = result.getJSONObject(i);
            object.put("user", userMap.get(object.getInteger("userId")));
            JSONArray childs = object.getJSONArray("child");
            if (childs != null && !childs.isEmpty()) {
                for (int j = 0; j < childs.size(); j++) {
                    JSONObject child = childs.getJSONObject(j);
                    Integer fromId = child.getInteger("fromId");
                    Integer toId = child.getInteger("toId");
                    child.put("fromUsername", userMap.get(fromId).getString("username"));
                    child.put("fromAvatar", userMap.get(fromId).getString("avatar"));
                    child.put("toUsername", userMap.get(toId).getString("username"));
                    child.put("toAvatar", userMap.get(toId).getString("avatar"));
                }
            }
        }
        return setResultSuccess(comments);
    }

    @Override
    public BaseResponse<JSONObject> insertChildComment(@RequestBody @Valid ChildCommentDTO childCommentDTO,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return setResultError(errorMsg);
        }
        if (parentCommentMapper.isExist(childCommentDTO.getParentId()) <= 0) {
            return setResultError("评论不存在或已删除");
        }
        if (!userServiceFeign.isExist(childCommentDTO.getFromId())) {
            return setResultError("用户不存在");
        }
        if (!userServiceFeign.isExist(childCommentDTO.getToId())) {
            return setResultError("用户不存在");
        }
        ChildComment childComment = new ChildComment();
        BeanUtils.copyProperties(childCommentDTO, childComment);
        childComment.setGoodCount(0);
        childComment.setCreateTime(System.currentTimeMillis());
        childComment.setIsDel(false);
        if (childCommentMapper.insertComment(childComment) > 0) {
            articleServiceFeign.incrementComment(childComment.getArticleId());
            parentCommentMapper.incrementComment(childComment.getParentId());
            return setResultSuccess("评论成功", (JSONObject) JSON.toJSON(childComment));
        }
        return setResultError("评论失败");
    }

    @Override
    public BaseResponse<JSONObject> deleteChildComment(Integer id, Integer userId, Integer articleId, Integer parentId) {
        if (id == null || id <= 0 || userId == null || userId <= 0) {
            return setResultError("无效的请求");
        }
        if (childCommentMapper.checkPermissions(id, userId) > 0) {
            if (childCommentMapper.deleteComment(id) > 0) {
                parentCommentMapper.decreaseComment(parentId);
                articleServiceFeign.decreaseComment(articleId);
                return setResultSuccess("删除评论成功");
            }
            return setResultError("删除评论失败，评论不存在或已删除");
        } else {
            return setResultError("您不能删除他人的评论");
        }
    }

    @Override
    public BaseResponse<JSONObject> getChildComment(Integer parentId, Integer userId, Integer offset, Integer limit) {
        JSONObject jsonObject = selectChildComment(parentId, userId, offset, limit).getData();
        JSONArray childArray = jsonObject.getJSONArray("child");
        Set<Integer> userIdSet = new HashSet<>();
        for (int i = 0; i < childArray.size(); i++) {
            JSONObject object = childArray.getJSONObject(i);
            userIdSet.add(object.getInteger("fromId"));
            userIdSet.add(object.getInteger("toId"));
        }
        List<Integer> userIdList = new ArrayList<>(userIdSet);
        JSONArray userArray = userServiceFeign.getSimpleUserByList(userIdList).getData().getJSONArray("users");
        Map<Integer, JSONObject> userMap = new HashMap<>(16);
        for (int i = 0; i < userArray.size(); i++) {
            JSONObject user = userArray.getJSONObject(i);
            userMap.put(user.getInteger("id"), user);
        }
        for (int i = 0; i < childArray.size(); i++) {
            JSONObject child = childArray.getJSONObject(i);
            Integer fromId = child.getInteger("fromId");
            Integer toId = child.getInteger("toId");
            child.put("fromUsername", userMap.get(fromId).getString("username"));
            child.put("fromAvatar", userMap.get(fromId).getString("avatar"));
            child.put("toUsername", userMap.get(toId).getString("username"));
            child.put("toAvatar", userMap.get(toId).getString("avatar"));
        }
        return setResultSuccess(jsonObject);
    }

    public BaseResponse<JSONObject> selectChildComment(Integer parentId, Integer userId, Integer offset, Integer limit) {
        if (offset < 0) {
            offset = 0;
        }
        if (limit < 2) {
            limit = 2;
        }
        PageHelper.offsetPage(offset, limit);
        List<ChildCommentVo> childCommentVos = childCommentMapper.selectChildComment(parentId, userId);
        Page<ChildCommentVo> pages = (Page<ChildCommentVo>)childCommentVos;
        JSONArray array = JSONObject.parseArray(JSONObject.toJSONString(childCommentVos));
        JSONObject result = new JSONObject();
        result.put("child", array);
        result.put("offset", pages.getEndRow());
        result.put("total", pages.getTotal());
        return setResultSuccess(result);
    }

    @Override
    public Boolean incrementGood(Integer id, Boolean type) {
        if (type) {
            return parentCommentMapper.incrementGood(id) > 0;
        } else {
            return childCommentMapper.incrementGood(id) > 0;
        }
    }

    @Override
    public Boolean decreaseGood(Integer id, Boolean type) {
        if (type) {
            return parentCommentMapper.decreaseGood(id) > 0;
        } else {
            return childCommentMapper.decreaseGood(id) > 0;
        }
    }
}
