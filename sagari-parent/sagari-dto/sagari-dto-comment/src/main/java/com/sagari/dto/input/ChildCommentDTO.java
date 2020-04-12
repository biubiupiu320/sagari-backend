package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author biubiupiu~
 */
@ApiModel(value = "生成子评论实体类")
public class ChildCommentDTO {

    @ApiModelProperty(value = "子评论对应的父评论的ID")
    @NotNull(message = "父评论ID不能为空")
    @Min(value = 1, message = "父评论ID无效")
    private Integer parentId;

    @ApiModelProperty(value = "非必须，则代表回复的是子评论")
    private Integer childId;

    @ApiModelProperty(value = "评论者ID")
    @NotNull(message = "评论者ID不能为空")
    @Min(value = 1, message = "评论者ID无效")
    private Integer fromId;

    @ApiModelProperty(value = "被评论者ID")
    @NotNull(message = "被评论者ID不能为空")
    @Min(value = 1, message = "被评论者ID无效")
    private Integer toId;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论不能为空")
    @Size(max = 255, message = "评论最多255个字")
    private String content;

    @ApiModelProperty(value = "文章作者ID")
    @NotNull(message = "文章作者ID不能为空")
    @Min(value = 1, message = "文章作者ID无效")
    private Integer authorId;

    @ApiModelProperty(value = "文章ID")
    @NotNull(message = "文章ID不能为空")
    @Min(value = 1, message = "文章ID无效")
    private Integer articleId;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChildCommentDTO{");
        sb.append("parentId=").append(parentId);
        sb.append(", childId=").append(childId);
        sb.append(", fromId=").append(fromId);
        sb.append(", toId=").append(toId);
        sb.append(", content='").append(content).append('\'');
        sb.append(", authorId=").append(authorId);
        sb.append(", articleId=").append(articleId);
        sb.append('}');
        return sb.toString();
    }
}
