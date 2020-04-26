package com.sagari.gateway.entity;

/**
 * @author biubiupiu~
 */
public class Ban {

    private Integer id;
    private Integer userId;
    private String reason;
    private Long startTime;
    private Long endTime;
    private Boolean isActive;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Ban{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", reason='").append(reason).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", isActive=").append(isActive);
        sb.append('}');
        return sb.toString();
    }
}
