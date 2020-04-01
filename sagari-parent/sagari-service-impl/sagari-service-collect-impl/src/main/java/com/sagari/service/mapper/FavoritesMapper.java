package com.sagari.service.mapper;

import com.sagari.service.entity.Favorites;
import com.sagari.service.entity.FavoritesVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface FavoritesMapper {

    @Insert("INSERT INTO favorites VALUES(null,#{userId},#{title},#{description},#{count},#{pri},#{createTime},#{isDel})")
    int createFavorites(Favorites favorites);

    @Update("UPDATE favorites SET title=#{title},description=#{description},pri=#{pri} " +
            "WHERE id=#{id} AND user_id=#{userId} AND is_del=FALSE")
    int modifyFavorites(Favorites favorites);

    @Update("UPDATE favorites SET is_del=TRUE WHERE id=#{id} AND user_id=#{userId} AND is_del=FALSE")
    int deleteFavorites(Integer id, Integer userId);

    @Select("SELECT COUNT(id) FROM favorites WHERE id=#{id} AND user_id=#{userId} AND is_del=FALSE")
    int checkPermissions(Integer id, Integer userId);

    @Select("SELECT id,user_id,title,description,count,pri FROM favorites " +
            "WHERE user_id=#{targetUserId} AND pri=FALSE AND is_del=FALSE")
    List<FavoritesVO> getPubFavorites(Integer targetUserId);

    @Select("SELECT id,user_id,title,description,count,pri FROM favorites " +
            "WHERE user_id=#{targetUserId} AND pri=TRUE AND is_del=FALSE")
    List<FavoritesVO> getPriFavorites(Integer targetUserId);

    @Select("SELECT COUNT(id) FROM favorites WHERE id=#{id} AND pri=FALSE")
    int isPub(Integer id);

    @Update("UPDATE favorites SET count=count+1 WHERE id=#{id}")
    int incrementCount(Integer id);

    @Update("UPDATE favorites SET count=count-1 WHERE id=#{id}")
    int decreaseCount(Integer id);

    @Update("UPDATE favorites SET count=count+#{count} WHERE id=#{id}")
    int incrementCountN(Integer id, Integer count);

    @Update("UPDATE favorites SET count=count-#{count} WHERE id=#{id}")
    int decreaseCountN(Integer id, Integer count);
}
