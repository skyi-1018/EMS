package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@TableName("biz_customer")
public class BizCustomer {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "客户名称不能为空")
    private String name;

    private String contactPerson;

    private String phone;

    private String address;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 封装客户单价Map
    @TableField(exist = false)
    private Map<Integer, Map<String, BigDecimal>> unitPriceMap;
}