package com.sagari.dto.input;

/**
 * @author biubiupiu~
 */
public class NoticeFollowDTO {

    private Integer fromId;
    private Integer toId;

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NoticeFollowDTO{");
        sb.append("fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append('}');
        return sb.toString();
    }
}
