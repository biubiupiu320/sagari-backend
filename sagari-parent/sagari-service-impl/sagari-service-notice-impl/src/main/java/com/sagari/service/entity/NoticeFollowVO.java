package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class NoticeFollowVO {

    private Integer id;
    private Integer fromId;
    private String fromUsername;
    private Integer toId;
    private String toUsername;
    private Long createTime;
    private Boolean isRead;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NoticeFollowVO{");
        sb.append("id=").append(id);
        sb.append(", fromId=").append(fromId);
        sb.append(", fromUsername='").append(fromUsername).append('\'');
        sb.append(", toId=").append(toId);
        sb.append(", toUsername='").append(toUsername).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", isRead=").append(isRead);
        sb.append('}');
        return sb.toString();
    }
}
