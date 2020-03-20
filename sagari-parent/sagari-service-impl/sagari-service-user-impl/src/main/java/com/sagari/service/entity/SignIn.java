package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
@Data
public class SignIn {

    private Integer id;
    private Integer userId;
    private String type;
    private String device;
    private String browser;
    private String system;
    private String ip;
    private Long time;
}
