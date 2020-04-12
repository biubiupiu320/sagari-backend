package com.sagari.service.mapper;

import com.sagari.service.entity.ChildComment;
import com.sagari.service.entity.ChildCommentVo;
import com.sagari.service.entity.ContentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface ChildCommentMapper {

    int insertComment(ChildComment childComment);

    int checkPermissions(@Param("id") Integer id,
                         @Param("userId") Integer userId);

    int deleteComment(@Param("id") Integer id);

    List<ChildCommentVo> selectChildComment(@Param("parentId") Integer parentId,
                                            @Param("userId") Integer userId);

    int incrementGood(@Param("id") Integer id);

    int decreaseGood(@Param("id") Integer id);

    List<ContentVO> getChildContent(@Param("ids") List<Integer> ids);
}
