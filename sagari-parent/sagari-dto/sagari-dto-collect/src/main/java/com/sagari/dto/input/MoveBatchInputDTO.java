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
@ApiModel(value = "移动收藏文章至其他收藏夹实体类")
public class MoveBatchInputDTO {

    @ApiModelProperty(value = "要移动的文章ID列表")
    @NotNull(message = "文章ID不能为空")
    @Size(min = 1, message = "无效的请求")
    private List<Integer> articleIds;

    @ApiModelProperty(value = "源收藏夹ID")
    @NotNull(message = "源收藏夹ID不能为空")
    @Min(value = 1, message = "无效的请求")
    private Integer source;

    @ApiModelProperty(value = "目标收藏夹ID")
    @NotNull(message = "目标收藏夹ID不能为空")
    @Min(value = 1, message = "无效的请求")
    private Integer target;

    public List<Integer> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<Integer> articleIds) {
        this.articleIds = articleIds;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "MoveBatchInputDTO{" +
                ", articleIds=" + articleIds +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
