package com.sagari.service.entity;

import lombok.Data;

/**
 * @author biubiupiu~
 */
public class FavoritesVO {

    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Integer count;
    private Boolean pri;

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

    @Override
    public String toString() {
        return "FavoritesVO{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", pri=" + pri +
                '}';
    }
}
