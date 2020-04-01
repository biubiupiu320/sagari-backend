package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author biubiupiu~
 */
@ApiModel(value = "收藏文章实体类")
public class CollectInputDTO {

    @ApiModelProperty(value = "收藏的文章ID")
    @NotNull(message = "文章ID不能为空")
    @Min(value = 1, message = "无效的请求")
    private Integer articleId;

    @ApiModelProperty(value = "收藏夹ID")
    @NotNull(message = "收藏夹ID不能为空")
    @Min(value = 1, message = "无效的请求")
    private Integer favoritesId;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(Integer favoritesId) {
        this.favoritesId = favoritesId;
    }

    @Override
    public String toString() {
        return "CollectInputDTO{" +
                ", articleId=" + articleId +
                ", favoritesId=" + favoritesId +
                '}';
    }
}
