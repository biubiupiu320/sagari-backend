package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private Integer articleCount;
    private Integer followCount;
    private Integer fansCount;
    private Long createTime;
    private Long updateTime;
}
