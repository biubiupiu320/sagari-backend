package com.sagari.gateway.mapper;

import com.sagari.gateway.entity.Ban;
import org.apache.ibatis.annotations.Param;

/**
 * @author biubiupiu~
 */
public interface BanMapper {
    Ban getBanInfo(@Param("userId") Integer userId);
}
