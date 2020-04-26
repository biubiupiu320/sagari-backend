package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class Letter {

    private Integer index;
    private Integer id;
    private Integer fromId;
    private String fromUsername;
    private String fromAvatar;
    private Integer toId;
    private String msg;
    private Long createTime;
    private Boolean isDel;
    private Integer status;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

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

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Letter{");
        sb.append("index=").append(index);
        sb.append(", id=").append(id);
        sb.append(", fromId=").append(fromId);
        sb.append(", fromUsername='").append(fromUsername).append('\'');
        sb.append(", fromAvatar='").append(fromAvatar).append('\'');
        sb.append(", toId=").append(toId);
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", isDel=").append(isDel);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
