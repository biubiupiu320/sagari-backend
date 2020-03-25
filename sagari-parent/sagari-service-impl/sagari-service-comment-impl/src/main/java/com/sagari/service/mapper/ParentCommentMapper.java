package com.sagari.service.mapper;

import com.sagari.service.entity.ParentComment;
import com.sagari.service.entity.ParentCommentVo;
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

    @Select("SELECT DISTINCT a.*,IFNULL(b.good,FALSE) AS good\n" +
            "FROM comment_parent a\n" +
            "LEFT JOIN\n" +
            "interactive_comment b\n" +
            "ON a.id=b.target_id AND b.type=true\n" +
            "WHERE\n" +
            "a.article_id=#{articleId}\n" +
            "AND\n" +
            "a.is_del=false\n" +
            "ORDER BY a.id")
    List<ParentCommentVo> selectParentComment(Integer articleId);

    @Select("select count(id) from comment_parent where id=#{id} and user_id=#{userId} and is_del=false")
    int checkPermissions(Integer id, Integer userId);

    @Select("select count(id) from comment_parent where id=#{id} and is_del=false")
    int isExist(Integer id);

    @Update("update comment_parent set comment_count=comment_count+1 where id=#{id} and is_del=false")
    int incrementComment(Integer id);

    @Update("update comment_parent set comment_count=comment_count-1 where id=#{id} and is_del=false")
    int decreaseComment(Integer id);
}
