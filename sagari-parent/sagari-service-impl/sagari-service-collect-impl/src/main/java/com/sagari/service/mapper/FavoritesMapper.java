package com.sagari.service.mapper;

import com.sagari.service.entity.Favorites;
import com.sagari.service.entity.FavoritesVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface FavoritesMapper {

    int createFavorites(Favorites favorites);

    int modifyFavorites(Favorites favorites);

    int deleteFavorites(@Param("id") Integer id,
                        @Param("userId") Integer userId);

    int checkPermissions(@Param("id") Integer id,
                         @Param("userId") Integer userId);

    List<FavoritesVO> getPubFavorites(@Param("targetUserId") Integer targetUserId);

    List<FavoritesVO> getPriFavorites(@Param("targetUserId") Integer targetUserId);

    int isPub(@Param("id") Integer id);

    int incrementCount(@Param("id") Integer id);

    int decreaseCount(@Param("id") Integer id);

    int incrementCountN(@Param("id") Integer id,
                        @Param("count") Integer count);

    int decreaseCountN(@Param("id") Integer id,
                       @Param("count") Integer count);
}
