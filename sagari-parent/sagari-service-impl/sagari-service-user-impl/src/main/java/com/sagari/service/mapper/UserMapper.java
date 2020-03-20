package com.sagari.service.mapper;

import com.sagari.service.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

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
}
