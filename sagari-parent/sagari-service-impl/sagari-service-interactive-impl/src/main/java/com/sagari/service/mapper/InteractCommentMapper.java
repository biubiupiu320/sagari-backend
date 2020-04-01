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

    @Insert("insert into interactive_comment " +
            "values(null,#{userId},#{targetId},#{type},#{good},#{createTime},#{updateTime})")
    int insert(Interactive interactive);

    @Delete("delete from interactive_comment where target_id=#{targetId} and user_id=#{userId} and type=#{flag}")
    int delete(@Param("targetId") Integer targetId, @Param("userId") Integer userId, @Param("flag") Boolean flag);

    @Select("select count(id) from interactive_comment " +
            "where target_id=#{targetId} and user_id=#{userId} and good=true and type=#{flag}")
    int isGood(@Param("targetId") Integer targetId, @Param("userId") Integer userId, @Param("flag") Boolean flag);
}
