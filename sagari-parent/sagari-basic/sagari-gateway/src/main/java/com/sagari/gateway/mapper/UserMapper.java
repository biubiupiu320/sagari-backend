package com.sagari.gateway.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author biubiupiu~
 */
public interface UserMapper {

    Integer getIdByUsername(@Param("username") String username);

    Integer getIdByPhone(@Param("phone") String phone);

    Integer getIdByEmail(@Param("email") String email);

    Integer getIdByQQ(@Param("qqId") String qqId);

}
