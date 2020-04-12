package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class NoticeSystemVO {

    private Integer id;
    private String title;
    private String content;
    private Long createTime;
    private Boolean isRead;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NoticeSystemVO{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", isRead=").append(isRead);
        sb.append('}');
        return sb.toString();
    }
}
