package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author biubiupiu~
 */
@ApiModel(value = "创建收藏夹实体类")
public class FavoritesInputDTO {

    @ApiModelProperty(value = "收藏夹ID,创建收藏夹时可不填,修改收藏夹必填")
    private Integer id;

    @ApiModelProperty(value = "收藏夹标题")
    @NotBlank(message = "收藏夹标题不能为空")
    @Size(max = 10, message = "收藏夹标题最多10个字")
    private String title;

    @ApiModelProperty(value = "收藏夹描述")
    @Size(max = 50, message = "收藏夹的描述最多50个字")
    private String description;

    @ApiModelProperty(value = "收藏夹是否私密")
    @NotNull(message = "收藏夹必须选择是否私密")
    private Boolean pri;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPri() {
        return pri;
    }

    public void setPri(Boolean pri) {
        this.pri = pri;
    }

    @Override
    public String toString() {
        return "FavoritesInputDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pri=" + pri +
                '}';
    }
}
