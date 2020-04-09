package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class ParentCommentVo {
    private Integer id;
    private Integer userId;
    private Integer articleId;
    private Integer authorId;
    private String content;
    private Integer goodCount;
    private Integer commentCount;
    private Long createTime;
    private Boolean isDel;
    private Boolean good;

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

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    public Boolean getGood() {
        return good;
    }

    public void setGood(Boolean good) {
        this.good = good;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ParentCommentVo{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", articleId=").append(articleId);
        sb.append(", authorId=").append(authorId);
        sb.append(", content='").append(content).append('\'');
        sb.append(", goodCount=").append(goodCount);
        sb.append(", commentCount=").append(commentCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", isDel=").append(isDel);
        sb.append(", good=").append(good);
        sb.append('}');
        return sb.toString();
    }
}
