package com.sagari.service.mapper;

import com.sagari.service.entity.Article;
import com.sagari.service.entity.ArticleVO;
import com.sagari.service.entity.TitleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface ArticleMapper {

    int publishArticle(Article article);

    ArticleVO selectArticle(@Param("id") Integer id);

    int updateArticle(Article article);

    int deleteArticle(@Param("id") Integer id,
                      @Param("author") Integer author);

    int isExist(@Param("id") Integer id);

    int checkPermissions(@Param("articleId") Integer articleId,
                         @Param("author") Integer author);

    int incrementGood(@Param("articleId") Integer articleId);

    int decreaseGood(@Param("articleId") Integer articleId);

    Integer getAuthor(@Param("articleId") Integer articleId);

    List<TitleVO> getTitle(@Param("ids") List<Integer> ids);

    int incrementComment(@Param("articleId") Integer articleId);

    int decreaseComment(@Param("articleId") Integer articleId);

    int incrementView(@Param("articleId") Integer articleId);

    int incrementCollect(@Param("articleId") Integer articleId);

    int decreaseCollect(@Param("articleId") Integer articleId);

    int incrementCollectN(@Param("ids") List<Integer> ids);

    int decreaseCollectN(@Param("ids") List<Integer> ids);

    List<Article> selectArticleList(@Param("articleIds") List<Integer> articleIds);

    String getArticleTags(@Param("id") Integer id);

    List<ArticleVO> getArticle(@Param("author") Integer author);

    List<ArticleVO> getArticleNotDel(@Param("author") Integer author);

    List<ArticleVO> getArticleInRecycle(@Param("author") Integer author);

    int delCompArticle(@Param("id") Integer id,
                       @Param("author") Integer author);

    int createDelCompRecord(ArticleVO articleVO);

    int restoreArticle(@Param("id") Integer id,
                       @Param("author") Integer author,
                       @Param("updateTime") Long updateTime);

    List<ArticleVO> getArticleByAuthor(@Param("author") Integer author);
}
