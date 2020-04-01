package com.sagari.service.mapper;

import com.sagari.service.entity.Article;
import com.sagari.service.entity.ArticleVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface ArticleMapper {

    @Insert("insert into article values(null,#{title},#{content},#{author},#{commentCount},#{viewCount}," +
            "#{goodCount},#{collectCount},#{tags},#{createTime},#{updateTime},#{isDel})")
    int publishArticle(Article article);

    ArticleVO selectArticle(Integer id);

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

    @Update("update article set comment_count=comment_count+1 where id=#{articleId}")
    int incrementComment(Integer articleId);

    @Update("update article set comment_count=comment_count-1 where id=#{articleId}")
    int decreaseComment(Integer articleId);

    @Update("update article set view_count=view_count+1 where id=#{articleId}")
    int incrementView(Integer articleId);

    @Update("update article set collect_count=collect_count+1 where id=#{articleId}")
    int incrementCollect(Integer articleId);

    @Update("update article set collect_count=collect_count+1 where id=#{articleId}")
    int decreaseCollect(Integer articleId);

    int incrementCollectN(@Param("ids") List<Integer> ids);

    int decreaseCollectN(@Param("ids") List<Integer> ids);

    List<Article> selectArticleList(@Param("articleIds") List<Integer> articleIds);
}
