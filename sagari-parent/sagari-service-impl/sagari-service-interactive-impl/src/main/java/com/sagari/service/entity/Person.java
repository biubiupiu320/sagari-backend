package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class Person {

    private Integer toId;
    private String toUsername;
    private String toAvatar;
    private String msg;
    private Long createTime;

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

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Person{");
        sb.append("toId=").append(toId);
        sb.append(", toUsername='").append(toUsername).append('\'');
        sb.append(", toAvatar='").append(toAvatar).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
