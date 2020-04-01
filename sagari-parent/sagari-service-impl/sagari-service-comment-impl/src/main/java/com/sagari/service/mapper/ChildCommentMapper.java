package com.sagari.service.mapper;

import com.sagari.service.entity.ChildComment;
import com.sagari.service.entity.ChildCommentVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface ChildCommentMapper {

    @Insert("insert into comment_child values(null,#{parentId},#{fromId},#{toId},#{content}," +
            "#{authorId},#{articleId},#{goodCount},#{createTime},#{isDel})")
    int insertComment(ChildComment childComment);

    @Select("select count(id) from comment_child where id=#{id} and author_id=#{userId} and is_del=false")
    int checkPermissions(Integer id, Integer userId);

    @Update("update comment_child set is_del=true where id=#{id} and is_del=false")
    int deleteComment(Integer id);

    @Select("SELECT DISTINCT a.*,IFNULL(b.good,FALSE) AS good\n" +
            "FROM comment_child a\n" +
            "LEFT JOIN\n" +
            "interactive_comment b\n" +
            "ON a.id=b.target_id AND b.type=FALSE AND b.user_id=#{userId}\n" +
            "WHERE\n" +
            "a.parent_id=#{parentId}\n" +
            "AND\n" +
            "a.is_del=FALSE\n" +
            "ORDER BY a.id")
    List<ChildCommentVo> selectChildComment(Integer parentId, Integer userId);

    @Update("update comment_child set good_count=good_count+1 where id=#{id} and is_del=false")
    int incrementGood(Integer id);

    @Update("update comment_child set good_count=good_count-1 where id=#{id} and is_del=false")
    int decreaseGood(Integer id);
}
