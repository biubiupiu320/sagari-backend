package com.sagari.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author biubiupiu~
 */
@Data
@ApiModel(value = "生成父评论实体类")
public class ParentCommentDTO {

    @ApiModelProperty(value = "发布父评论的用户ID")
    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID无效")
    private Integer userId;

    @ApiModelProperty(value = "将评论发布到文章的ID")
    @NotNull(message = "文章ID不能为空")
    @Min(value = 1, message = "文章ID无效")
    private Integer articleId;

    @ApiModelProperty(value = "评论内容，最多255个字")
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 255, message = "评论最多255个字")
    private String content;
}
