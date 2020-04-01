package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class Collect {

    private Integer id;
    private Integer userId;
    private Integer articleId;
    private Integer favoritesId;
    private Long createTime;
    private Long updateTime;
    private Boolean isDel;

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

    public Integer getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(Integer favoritesId) {
        this.favoritesId = favoritesId;
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

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", userId=" + userId +
                ", articleId=" + articleId +
                ", favoritesId=" + favoritesId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDel=" + isDel +
                '}';
    }
}
