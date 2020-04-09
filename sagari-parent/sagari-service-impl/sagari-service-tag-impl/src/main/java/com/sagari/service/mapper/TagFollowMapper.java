package com.sagari.service.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface TagFollowMapper {

    int isFollow(@Param("tagId") Integer tagId, @Param("userId") Integer userId);

    int follow(@Param("tagId") Integer tagId,
               @Param("userId") Integer userId,
               @Param("createTime") Long createTime);

    int cancelFollow(@Param("tagId") Integer tagId, @Param("userId") Integer userId);

    List<Integer> getFollow(@Param("userId") Integer userId);
}
