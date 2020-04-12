package com.sagari.dto.input;

/**
 * @author biubiupiu~
 */
public class NoticeGoodDTO {

    private Integer fromId;
    private Integer toId;
    private Integer type;
    private Integer targetId;
    private Integer articleId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NoticeGoodDTO{");
        sb.append("fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", type=").append(type);
        sb.append(", targetId=").append(targetId);
        sb.append(", articleId=").append(articleId);
        sb.append('}');
        return sb.toString();
    }
}
