package com.sagari.service.mapper;

import com.sagari.service.entity.NoticeSystem;
import com.sagari.service.entity.NoticeSystemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface NoticeSystemMapper {

    int createNoticeSystem(NoticeSystem noticeSystem);

    List<NoticeSystemVO> getNoticeSystem(@Param("userId") Integer userId);

    List<Integer> getRead(@Param("ids") List<Integer> ids,
                          @Param("userId") Integer userId);

    int markReadSystem(@Param("ids") List<Integer> ids,
                       @Param("userId") Integer userId,
                       @Param("createTime") Long createTime);

    List<Integer> getAllId(@Param("userId") Integer userId);

    int unreadNoticeCount(@Param("userId") Integer userId);

}
