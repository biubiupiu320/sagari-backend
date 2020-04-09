package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class TagVO {

    private Integer id;
    private Integer categoryId;
    private String title;
    private String description;
    private Integer articleCount;
    private Boolean follow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TagVO{");
        sb.append("id=").append(id);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", articleCount=").append(articleCount);
        sb.append(", follow=").append(follow);
        sb.append('}');
        return sb.toString();
    }
}
