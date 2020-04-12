package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class NoticeCommentVO {

    private Integer id;
    private Integer fromId;
    private String fromUsername;
    private Integer toId;
    private String toUsername;
    private Integer type;
    private Integer contentId;
    private String content;
    private Integer targetId;
    private Integer articleId;
    private Long createTime;
    private Boolean isRead;
    private String text;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NoticeCommentVO{");
        sb.append("id=").append(id);
        sb.append(", fromId=").append(fromId);
        sb.append(", fromUsername='").append(fromUsername).append('\'');
        sb.append(", toId=").append(toId);
        sb.append(", toUsername='").append(toUsername).append('\'');
        sb.append(", type=").append(type);
        sb.append(", contentId=").append(contentId);
        sb.append(", content='").append(content).append('\'');
        sb.append(", targetId=").append(targetId);
        sb.append(", articleId=").append(articleId);
        sb.append(", createTime=").append(createTime);
        sb.append(", isRead=").append(isRead);
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
