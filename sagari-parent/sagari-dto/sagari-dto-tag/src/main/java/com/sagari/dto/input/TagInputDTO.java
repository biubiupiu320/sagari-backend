package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author biubiupiu~
 */
@ApiModel(value = "创建标签实体类")
public class TagInputDTO {

    @ApiModelProperty(value = "标签所属的分类ID")
    @NotNull(message = "标签所属的分类ID不能为空")
    @Min(value = 1, message = "无效的请求")
    private Integer categoryId;

    @ApiModelProperty(value = "标签的标题")
    @NotNull(message = "标签的标题不能为空")
    @Size(max = 15, message = "标签标题最多15个字")
    private String title;

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

    @Override
    public String toString() {
        return "TagInputDTO{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                '}';
    }
}
