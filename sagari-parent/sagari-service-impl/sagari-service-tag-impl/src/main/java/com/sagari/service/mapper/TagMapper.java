package com.sagari.service.mapper;

import com.sagari.service.entity.Tag;
import com.sagari.service.entity.TagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface TagMapper {

    int createTag(Tag tag);

    List<TagVO> getTag();

    List<TagVO> getHotTag();

    List<TagVO> getTagBatch(@Param("tagIds") List<Integer> tagIds);

    int incrArticleCount(@Param("tagIds") List<Integer> tagIds);

    List<Integer> getTagsByCategory(@Param("categoryId") Integer categoryId);
}
