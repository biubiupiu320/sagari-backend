package com.sagari.service.mapper;

import com.sagari.service.entity.User;
import com.sagari.service.entity.UserVO;
import com.sagari.service.entity.UsernameRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface UserMapper {

    int signUp(User user);

    User signInByPhone(@Param("phone") String phone);

    User signInByEmail(@Param("email") String email);

    User signInByUsername(@Param("username") String username);

    int isExist(@Param("id") Integer id);

    User getSimpleUser(@Param("id") Integer id);

    List<User> getSimpleUserByList(@Param("ids") List<Integer> ids);

    int isExistByPhone(@Param("phone") String phone);

    int isExistByUsername(@Param("username") String username);

    int isExistByEmail(@Param("email") String email);

    int incrementArticleCount(@Param("id") Integer id);

    int incrementFollowCount(@Param("id") Integer id);

    int decreaseFollowCount(@Param("id") Integer id);

    int incrementFollowCountN(@Param("id") Integer id, @Param("count") Integer count);

    int decreaseFollowCountN(@Param("id") Integer id, @Param("count") Integer count);

    int incrementFollowCountBatch(@Param("ids") List<Integer> ids);

    int decreaseFollowCountBatch(@Param("ids") List<Integer> ids);

    int incrementFansCount(@Param("id") Integer id);

    int decreaseFansCount(@Param("id") Integer id);

    int incrementFansCountN(@Param("id") Integer id, @Param("count") Integer count);

    int decreaseFansCountN(@Param("id") Integer id, @Param("count") Integer count);

    int incrementFansCountBatch(@Param("ids") List<Integer> ids);

    int decreaseFansCountBatch(@Param("ids") List<Integer> ids);

    UserVO getUserAll(@Param("id") Integer id);

    int modifyUser(User user);

    int insertUsernameRecord(UsernameRecord usernameRecord);

    Long getLastRecord(@Param("userId") Integer userId);

    int modifyPassword(@Param("id") Integer id,
                       @Param("password") String password,
                       @Param("updateTime") Long updateTime);

    String getPhone(@Param("id") Integer id);

    UserVO getUserByQQ(@Param("openId") String openId);

    int bindQQByUsername(@Param("username") String username,
                         @Param("qqId") String qqId);

    int bindQQByPhone(@Param("phone") String phone,
                      @Param("qqId") String qqId);

    int modifyAvatar(@Param("id") Integer id,
                     @Param("avatar") String avatar);

    String getFollowTags(@Param("id") Integer id);
}
