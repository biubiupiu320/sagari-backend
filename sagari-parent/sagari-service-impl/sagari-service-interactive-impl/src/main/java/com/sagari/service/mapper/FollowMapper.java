package com.sagari.service.mapper;

import com.sagari.service.entity.Follow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface FollowMapper {

    int follow(Follow follow);

    int cancelFollow(@Param("followIds") List<Integer> followIds,
                     @Param("fansId") Integer fansId,
                     @Param("updateTime") Long updateTime);

    int isFollow(@Param("followId") Integer followId, @Param("fansId") Integer fansId);

    List<Integer> isFollowList(@Param("fansId") Integer fansId,
                               @Param("followIds") List<Integer> followIds);

    int removeFans(@Param("followId") Integer followId,
                   @Param("fansIds") List<Integer> fansIds,
                   @Param("updateTime") Long updateTime);

    List<Integer> getFollowList(@Param("fansId") Integer fansId);

    List<Integer> getFansList(@Param("followId") Integer followId);
}
