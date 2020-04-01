package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author biubiupiu~
 */
@ApiModel(value = "取消收藏文章实体类")
public class CollectBatchInputDTO {

    @ApiModelProperty(value = "取消收藏的文章ID")
    @NotNull(message = "文章ID不能为空")
    @Size(min = 1, message = "无效的请求")
    private List<Integer> articleIds;

    @ApiModelProperty(value = "收藏夹ID")
    @NotNull(message = "收藏夹ID不能为空")
    @Min(value = 1, message = "无效的请求")
    private Integer favoritesId;

    public List<Integer> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<Integer> articleIds) {
        this.articleIds = articleIds;
    }

    public Integer getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(Integer favoritesId) {
        this.favoritesId = favoritesId;
    }

    @Override
    public String toString() {
        return "CollectBatchInputDTO{" +
                ", articleIds=" + articleIds +
                ", favoritesId=" + favoritesId +
                '}';
    }
}
