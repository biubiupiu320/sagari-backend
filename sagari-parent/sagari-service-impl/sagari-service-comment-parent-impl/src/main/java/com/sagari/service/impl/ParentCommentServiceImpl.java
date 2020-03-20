package com.sagari.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sagari.common.base.BaseApiService;
import com.sagari.common.base.BaseResponse;
import com.sagari.dto.input.ParentCommentDTO;
import com.sagari.service.ParentCommentService;
import com.sagari.service.entity.ParentComment;
import com.sagari.service.feign.ArticleServiceFeign;
import com.sagari.service.feign.InteractiveServiceFeign;
import com.sagari.service.feign.UserServiceFeign;
import com.sagari.service.mapper.ParentCommentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author biubiupiu~
 */
@RestController
public class ParentCommentServiceImpl extends BaseApiService<JSONObject> implements ParentCommentService {

    @Autowired
    private ParentCommentMapper commentMapper;
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private ArticleServiceFeign articleServiceFeign;
    @Autowired
    private InteractiveServiceFeign interactiveServiceFeign;

    @Override
    public BaseResponse<JSONObject> insertComment(@RequestBody @Valid ParentCommentDTO parentCommentDTO,
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
        if (commentMapper.insertComment(parentComment) > 0) {
            return setResultSuccess("评论成功");
        }
        return setResultError("评论失败");
    }

    @Override
    public BaseResponse<JSONObject> deleteComment(Integer id, Integer userId) {
        if (id == null || id <= 0 || userId == null || userId <= 0) {
            return setResultError("无效的请求");
        }
        if (commentMapper.checkPermissions(id, userId) > 0) {
            if (commentMapper.deleteComment(id) > 0) {
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
        List<ParentComment> list = commentMapper.selectComment(articleId);
        List<Integer> targetIds = list.stream().map(ParentComment::getId).collect(Collectors.toList());
        Map<Integer, Boolean> goodMap = interactiveServiceFeign.isGood(targetIds, userId, 2);
        PageInfo<ParentComment> comments = new PageInfo<>(list);
        JSONObject result = (JSONObject) JSON.toJSON(comments);
        JSONArray array = result.getJSONArray("list");
        for (int i = 0; i < array.size(); i++) {
            Boolean isGood = goodMap.get(array.getJSONObject(i).get("id"));
            if (isGood == null || !isGood) {
                array.getJSONObject(i).put("isGood", false);
            } else {
                array.getJSONObject(i).put("isGood", true);
            }
        }
        return setResultSuccess(result);
    }
}
