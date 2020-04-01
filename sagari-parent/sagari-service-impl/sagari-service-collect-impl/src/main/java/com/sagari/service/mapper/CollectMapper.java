package com.sagari.service.mapper;

import com.sagari.service.entity.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface CollectMapper {

    int collectArticle(Collect collect);

    int cancelCollectArticle(@Param("favoritesId") Integer favoritesId,
                             @Param("articleIds") List<Integer> articleIds);

    int moveToFavorites(@Param("source") Integer source,
                        @Param("target") Integer target,
                        @Param("articleIds") List<Integer> articleIds);

    List<Collect> getCollect(@Param("favoritesId") Integer favoritesId);

    int isCollect(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
}
