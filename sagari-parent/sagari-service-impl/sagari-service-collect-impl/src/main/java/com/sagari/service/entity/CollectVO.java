package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class CollectVO {

    private Integer articleId;
    private Integer articleTitle;
    private String articleContent;
    private Integer goodCount;
    private Integer commentCount;
    private Integer viewCount;
    private User user;
    private Boolean isChecked;

    public CollectVO() {
        this.isChecked = false;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(Integer articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "CollectVO{" +
                "articleId=" + articleId +
                ", articleTitle=" + articleTitle +
                ", articleContent='" + articleContent + '\'' +
                ", goodCount=" + goodCount +
                ", commentCount=" + commentCount +
                ", viewCount=" + viewCount +
                ", user=" + user +
                ", isChecked=" + isChecked +
                '}';
    }
}
