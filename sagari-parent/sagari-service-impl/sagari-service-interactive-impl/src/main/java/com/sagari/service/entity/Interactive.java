package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class Interactive {

    private Integer id;
    private Integer userId;
    private Integer targetId;
    private Boolean type;
    private Boolean good;
    private Long createTime;
    private Long updateTime;

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

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Boolean getGood() {
        return good;
    }

    public void setGood(Boolean good) {
        this.good = good;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Interactive{" +
                "id=" + id +
                ", userId=" + userId +
                ", targetId=" + targetId +
                ", type=" + type +
                ", good=" + good +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
