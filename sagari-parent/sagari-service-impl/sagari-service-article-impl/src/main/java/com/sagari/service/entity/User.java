package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String avatar;
    private Integer articleCount;
    private Integer fansCount;
}
