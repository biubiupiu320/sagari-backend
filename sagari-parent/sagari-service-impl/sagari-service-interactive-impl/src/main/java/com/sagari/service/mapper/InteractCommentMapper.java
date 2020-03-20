package com.sagari.service.mapper;

import com.sagari.service.entity.Interactive;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author biubiupiu~
 */
public interface InteractCommentMapper {
    int insert(Interactive interactive);

    int delete(@Param("targetId") Integer targetId, @Param("userId") Integer userId, @Param("flag") Boolean flag);

    int isGood(@Param("targetId") Integer targetId, @Param("userId") Integer userId, @Param("flag") Boolean flag);

    List<Map<String, Object>> isGoodList(List<Integer> list, Integer userId, Boolean flag);
}
