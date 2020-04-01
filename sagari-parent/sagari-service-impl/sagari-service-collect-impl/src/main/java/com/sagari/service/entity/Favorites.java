package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class Favorites {

    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Integer count;
    private Boolean pri;
    private Long createTime;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getPri() {
        return pri;
    }

    public void setPri(Boolean pri) {
        this.pri = pri;
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

    @Override
    public String toString() {
        return "Favorites{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", pri=" + pri +
                ", createTime=" + createTime +
                ", isDel=" + isDel +
                '}';
    }
}
