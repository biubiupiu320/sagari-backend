package com.sagari.service.mapper;

import com.sagari.service.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface UserMapper {

    @Insert("insert into `user` values(null,#{username},#{password},#{email},#{phone},#{avatar}," +
            "#{articleCount},#{followCount},#{fansCount},#{createTime},#{updateTime})")
    int signUp(User user);

    @Select("select * from `user` where phone=#{phone}")
    User signInByPhone(String phone);

    @Select("select * from `user` where email=#{email}")
    User signInByEmail(String email);

    @Select("select * from `user` where username=#{username}")
    User signInByUsername(String username);

    @Select("select count(id) from `user` where id=#{id}")
    int isExist(Integer id);

    @Select("select id,username,avatar,article_count,follow_count,fans_count from `user` where id=#{id}")
    User getSimpleUser(Integer id);

    List<User> getSimpleUserByList(List<Integer> list);

    @Select("select count(id) from `user` where phone=#{phone}")
    int isExistByPhone(String phone);

    @Select("select count(id) from `user` where username=#{username}")
    int isExistByUsername(String username);

    @Select("select count(id) from `user` where email=#{email}")
    int isExistByEmail(String email);

    @Update("update `user` set article_count=article_count+1 where id=#{id}")
    int incrementArticleCount(Integer id);

    @Update("UPDATE `user` SET follow_count=follow_count+1 WHERE id=#{id}")
    int incrementFollowCount(Integer id);

    @Update("UPDATE `user` SET follow_count=follow_count-1 WHERE id=#{id}")
    int decreaseFollowCount(Integer id);

    @Update("UPDATE `user` SET follow_count=follow_count+#{count} WHERE id=#{id}")
    int incrementFollowCountN(Integer id, Integer count);

    @Update("UPDATE `user` SET follow_count=follow_count-#{count} WHERE id=#{id}")
    int decreaseFollowCountN(Integer id, Integer count);

    int incrementFollowCountBatch(@Param("ids") List<Integer> ids);

    int decreaseFollowCountBatch(@Param("ids") List<Integer> ids);

    @Update("UPDATE `user` SET fans_count=fans_count+1 WHERE id=#{id}")
    int incrementFansCount(Integer id);

    @Update("UPDATE `user` SET fans_count=fans_count-1 WHERE id=#{id}")
    int decreaseFansCount(Integer id);

    @Update("UPDATE `user` SET fans_count=fans_count+#{count} WHERE id=#{id}")
    int incrementFansCountN(Integer id, Integer count);

    @Update("UPDATE `user` SET fans_count=fans_count-#{count} WHERE id=#{id}")
    int decreaseFansCountN(Integer id, Integer count);

    int incrementFansCountBatch(@Param("ids") List<Integer> ids);

    int decreaseFansCountBatch(@Param("ids") List<Integer> ids);
}
