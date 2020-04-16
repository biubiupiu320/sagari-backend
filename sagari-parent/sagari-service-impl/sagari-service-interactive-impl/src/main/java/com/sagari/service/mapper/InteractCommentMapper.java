package com.sagari.service.mapper;

import com.sagari.service.entity.Interactive;
import org.apache.ibatis.annotations.Param;

/**
 * @author biubiupiu~
 */
public interface InteractCommentMapper {

    int insert(Interactive interactive);

    int delete(@Param("targetId") Integer targetId,
               @Param("userId") Integer userId,
               @Param("flag") Boolean flag);

    int isGood(@Param("targetId") Integer targetId,
               @Param("userId") Integer userId,
               @Param("flag") Boolean flag);
}
