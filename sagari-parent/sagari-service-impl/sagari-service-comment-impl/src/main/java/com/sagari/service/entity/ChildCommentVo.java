package com.sagari.service.entity;

/**
 * @author biubiupiu~
 */
public class ChildCommentVo {
    private Integer id;
    private Integer parentId;
    private Integer fromId;
    private Integer toId;
    private String content;
    private Integer authorId;
    private Integer articleId;
    private Integer goodCount;
    private Long createTime;
    private Boolean isDel;
    private Boolean good;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(Integer goodCount) {
        this.goodCount = goodCount;
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
        final StringBuffer sb = new StringBuffer("ChildCommentVo{");
        sb.append("id=").append(id);
        sb.append(", parentId=").append(parentId);
        sb.append(", fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", content='").append(content).append('\'');
        sb.append(", authorId=").append(authorId);
        sb.append(", articleId=").append(articleId);
        sb.append(", goodCount=").append(goodCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", isDel=").append(isDel);
        sb.append(", good=").append(good);
        sb.append('}');
        return sb.toString();
    }
}
