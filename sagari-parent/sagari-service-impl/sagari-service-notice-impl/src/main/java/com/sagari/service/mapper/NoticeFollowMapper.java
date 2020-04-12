package com.sagari.service.mapper;

import com.sagari.service.entity.NoticeFollow;
import com.sagari.service.entity.NoticeFollowVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface NoticeFollowMapper {

    List<NoticeFollowVO> getNotice(@Param("userId") Integer userId);

    int createNotice(NoticeFollow noticeFollow);

    int markReadNotice(@Param("ids") List<Integer> ids,
                       @Param("userId") Integer userId,
                       @Param("updateTime") Long updateTime);

    int deleteNotice(@Param("ids") List<Integer> ids,
                     @Param("userId") Integer userId,
                     @Param("updateTime") Long updateTime);

    int markReadNoticeAll(@Param("userId") Integer userId,
                          @Param("updateTime") Long updateTime);

    int deleteNoticeAll(@Param("userId") Integer userId,
                        @Param("updateTime") Long updateTime);
}
