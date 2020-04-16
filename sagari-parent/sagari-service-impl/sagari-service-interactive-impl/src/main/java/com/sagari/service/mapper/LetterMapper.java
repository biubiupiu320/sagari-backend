package com.sagari.service.mapper;

import com.sagari.service.entity.Letter;
import com.sagari.service.entity.Person;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author biubiupiu~
 */
public interface LetterMapper {

    int createMsg(Letter letter);

    List<Person> getPersonList(@Param("fromId") Integer fromId);

    List<Letter> getLetters(@Param("fromId") Integer fromId,
                            @Param("toId") Integer toId);
}
