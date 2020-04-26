package com.sagari.service.mapper;

import com.sagari.service.entity.NoticeGood;
import com.sagari.service.entity.NoticeGoodVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface NoticeGoodMapper {

    List<NoticeGoodVO> getNotice(@Param("userId") Integer userId);

    int createNotice(NoticeGood noticeGood);

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

    int unreadNoticeCount(@Param("userId") Integer userId);
}
