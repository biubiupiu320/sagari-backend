package com.sagari.service.mapper;

import com.sagari.service.entity.ParentComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface ParentCommentMapper {

    @Insert("insert into comment_parent values(null,#{userId},#{articleId},#{authorId},#{content}," +
            "#{goodCount},#{commentCount},#{createTime},#{isDel})")
    int insertComment(ParentComment parentComment);

    @Update("update comment_parent set is_del=true where id=#{id} and is_del=false")
    int deleteComment(Integer id);

    @Select("select * from comment_parent where article_id=#{articleId} and is_del=false")
    List<ParentComment> selectComment(Integer articleId);

    @Select("select count(id) from comment_parent where id=#{id} and user_id=#{userId} and is_del=false")
    int checkPermissions(Integer id, Integer userId);
}
