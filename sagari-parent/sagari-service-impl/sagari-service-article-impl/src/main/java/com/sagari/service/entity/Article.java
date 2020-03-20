package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class Article {

    private Integer id;
    private String title;
    private String content;
    private Integer author;
    private Integer commentCount;
    private Integer viewCount;
    private Integer goodCount;
    private Integer collectCount;
    private String tags;
    private Long createTime;
    private Long updateTime;
    private Boolean isDel;
}
