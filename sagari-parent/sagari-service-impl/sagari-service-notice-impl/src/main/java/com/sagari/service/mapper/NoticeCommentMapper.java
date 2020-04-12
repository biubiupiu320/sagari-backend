package com.sagari.service.mapper;

import com.sagari.service.entity.NoticeComment;
import com.sagari.service.entity.NoticeCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface NoticeCommentMapper {

    List<NoticeCommentVO> getNotice(@Param("userId") Integer userId);

    int createNotice(NoticeComment noticeComment);

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
