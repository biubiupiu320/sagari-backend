package com.sagari.service.mapper;

import com.sagari.service.entity.ContentVO;
import com.sagari.service.entity.ParentComment;
import com.sagari.service.entity.ParentCommentVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface ParentCommentMapper {

    int insertComment(ParentComment parentComment);

    int deleteComment(@Param("id") Integer id);

    List<ParentCommentVo> selectParentComment(@Param("articleId") Integer articleId,
                                              @Param("userId") Integer userId);

    int checkPermissions(@Param("id") Integer id,
                         @Param("userId") Integer userId);

    int isExist(@Param("id") Integer id);

    int incrementComment(@Param("id") Integer id);

    int decreaseComment(@Param("id") Integer id);

    int incrementGood(@Param("id") Integer id);

    int decreaseGood(@Param("id") Integer id);

    List<ContentVO> getParentContent(@Param("ids") List<Integer> ids);
}
