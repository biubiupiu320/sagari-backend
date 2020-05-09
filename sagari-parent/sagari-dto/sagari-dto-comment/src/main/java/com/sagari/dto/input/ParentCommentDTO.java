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
@ApiModel(value = "生成父评论实体类")
public class ParentCommentDTO {

    @ApiModelProperty(value = "将评论发布到文章的ID")
    @NotNull(message = "文章ID不能为空")
    @Min(value = 1, message = "文章ID无效")
    private Integer articleId;

    @ApiModelProperty(value = "评论内容，最多255个字")
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 255, message = "评论最多255个字")
    private String content;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ParentCommentDTO{");
        sb.append("articleId=").append(articleId);
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
