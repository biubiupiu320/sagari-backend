package com.sagari.service.mapper;

import com.sagari.service.entity.Ban;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface BanMapper {

    List<Ban> getBanRecord(@Param("userId") Integer userId);

    Ban getBanInfo(@Param("userId") Integer userId);

}
