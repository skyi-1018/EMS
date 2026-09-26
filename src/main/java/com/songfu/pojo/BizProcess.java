package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("biz_process")
public class BizProcess {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "工艺名称不得为空")
    private String name;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
