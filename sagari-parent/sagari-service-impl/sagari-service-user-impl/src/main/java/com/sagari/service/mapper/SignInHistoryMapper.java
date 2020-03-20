package com.sagari.service.mapper;

import com.sagari.service.entity.SignIn;
import org.apache.ibatis.annotations.Insert;

/**
 * @author biubiupiu~
 */
public interface SignInHistoryMapper {

    @Insert("insert into signin_history values(null,#{userId},#{type},#{device},#{browser},#{system},#{ip},#{time})")
    int insertRecord(SignIn signIn);
}
