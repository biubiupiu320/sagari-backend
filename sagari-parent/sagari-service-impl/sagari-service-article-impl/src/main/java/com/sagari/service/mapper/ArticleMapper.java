package com.sagari.service.mapper;

import com.sagari.service.entity.Article;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author biubiupiu~
 */
public interface ArticleMapper {

    @Insert("insert into article values(null,#{title},#{content},#{creator},#{commentCount},#{viewCount}," +
            "#{goodCount},#{badCount},#{collectCount},#{tags},#{createTime},#{updateTime},#{isDel})")
    int publishArticle(Article article);

    @Select("select * from article where id=#{id} and is_del=false")
    Article selectArticle(Integer id);

    @Update("update article set title=#{title},content=#{content},tags=#{tags},update_time=#{updateTime} " +
            "where id=#{id} and creator=#{creator} and is_del=false")
    int updateArticle(Article article);

    @Update("update article set is_del=true where id=#{id}")
    int deleteArticle(Integer id);

    @Select("select count(id) from article where id=#{id} and is_del=false")
    int isExist(Integer id);

    @Select("select count(id) from article where id=#{articleId} and creator=#{creator}")
    int checkPermissions(Integer articleId, Integer creator);

    @Update("update article set good_count=good_count+1 where id=#{articleId}")
    int incrementGood(Integer articleId);

    @Update("update article set good_count=good_count-1 where id=#{articleId}")
    int decreaseGood(Integer articleId);

    @Select("select author from article where id=#{articleId} and is_del=false")
    Integer getAuthor(Integer articleId);
}
