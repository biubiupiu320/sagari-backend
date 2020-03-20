package com.sagari.service.mapper;

import com.sagari.service.entity.Interactive;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author biubiupiu~
 */
public interface InteractArticleMapper {

    @Insert("insert into interactive_article values(null,#{userId},#{targetId},#{good},#{createTime},#{updateTime})")
    int insert(Interactive interactive);

    @Delete("delete from  interactive_article where target_id=#{targetId} and user_id=#{userId}")
    int delete(Integer targetId, Integer userId);

    @Select("select count(id) from interactive_article " +
            "where target_id=#{targetId} and user_id=#{userId} and good=true")
    int isGood(Integer targetId, Integer userId);
}
