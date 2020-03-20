package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class ParentComment {

    private Integer id;
    private Integer userId;
    private Integer articleId;
    private Integer authorId;
    private String content;
    private Integer goodCount;
    private Integer commentCount;
    private Long createTime;
    private Boolean isDel;
}
