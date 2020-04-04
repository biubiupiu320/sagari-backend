package com.sagari.service.mapper;

import com.sagari.service.entity.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface CategoryMapper {

    int incrementTagCount(@Param("id") Integer id);

    List<CategoryVO> getCategory();

}
