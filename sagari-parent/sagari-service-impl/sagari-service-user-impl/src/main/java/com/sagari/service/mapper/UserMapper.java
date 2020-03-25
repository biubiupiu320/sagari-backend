package com.sagari.service.mapper;

import com.sagari.service.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
