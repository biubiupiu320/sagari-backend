package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class ChildCommentVo {
    private Integer id;
    private Integer parentId;
    private Integer fromId;
    private Integer toId;
    private String content;
    private Integer authorId;
    private Integer articleId;
    private Integer goodCount;
    private Long createTime;
    private Boolean isDel;
    private Boolean good;
}
