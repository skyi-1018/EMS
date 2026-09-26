package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_operation_log")
public class SysOperationLog {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;      // 操作人ID

    private String operation;    // 操作描述（来自注解）

    private String method;       // 请求方法 类名.方法名

    private String params;       // 请求参数(JSON)

    private String ip;           // 客户端IP

    private Integer status;      // 1成功 0失败

    private String errorMsg;     // 失败原因

    private Long costTime;       // 耗时(毫秒)

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String username;     // 操作人用户名
}
