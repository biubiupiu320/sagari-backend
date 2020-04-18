package com.sagari.service.mapper;

import com.sagari.service.entity.SignIn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface SignInHistoryMapper {

    int insertRecord(SignIn signIn);

    List<SignIn> getHistory(@Param("userId") Integer userId);
}
