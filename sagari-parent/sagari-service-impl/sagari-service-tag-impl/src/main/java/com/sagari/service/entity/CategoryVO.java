package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class CategoryVO {

    private Integer id;
    private String title;
    private Integer tagCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }

    @Override
    public String toString() {
        return "CategoryVO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tagCount=" + tagCount +
                '}';
    }
}
