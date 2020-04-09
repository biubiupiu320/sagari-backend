package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class UsernameRecord {

    private Integer id;
    private Integer userId;
    private String oldUsername;
    private String newUsername;
    private Long createTime;

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

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UsernameRecord{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", oldUsername='").append(oldUsername).append('\'');
        sb.append(", newUsername='").append(newUsername).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
