package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "sys_update_log", autoResultMap = true)
public class SysUpdateLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "版本号不能为空")
    private String version;
    @NotBlank(message = "更新日期不能为空")
    private LocalDate updateDate;
    @TableField(value = "update_contents", typeHandler = JacksonTypeHandler.class)
    private List<String> updateContents;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
