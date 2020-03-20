package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class Interactive {

    private Integer id;
    private Integer userId;
    private Integer targetId;
    private Boolean type;
    private Boolean good;
    private Long createTime;
    private Long updateTime;
}
