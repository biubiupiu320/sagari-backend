package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author biubiupiu~
 */
@ApiModel(value = "发布或修改文章实体类")
public class ArticleInputDTO {

    @ApiModelProperty(value = "文章ID，发布文章时可以不填，修改文章时必须以路径参数的形式传入")
    private Integer id;

    @ApiModelProperty(value = "文章标题")
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 25, message = "文章标题最多为25个字")
    private String title;

    @ApiModelProperty(value = "文章内容")
    @NotBlank(message = "文章内容不能为空")
    private String content;

    @ApiModelProperty(value = "文章标签(至少一个)")
    @NotBlank(message = "文章标签不能为空")
    private String tags;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ArticleInputDTO{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", tags='").append(tags).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
