package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class SignIn {

    private Integer id;
    private Integer userId;
    private String type;
    private String device;
    private String browser;
    private String system;
    private String ip;
    private Long time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SignIn{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", type='").append(type).append('\'');
        sb.append(", device='").append(device).append('\'');
        sb.append(", browser='").append(browser).append('\'');
        sb.append(", system='").append(system).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}
