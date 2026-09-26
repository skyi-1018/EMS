package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("biz_order")
public class BizOrder {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "客户ID不能为空")
    private Integer customerId;

    @NotNull(message = "工艺ID不能为空")
    private Integer processId;

    @NotNull(message = "订单日期不能为空")
    private LocalDate orderDate;

    @NotBlank(message = "产品名称不能为空")
    private String productName;

    @NotNull(message = "规格1不能为空")
    @TableField("spec_1")
    private BigDecimal spec1;

    @NotNull(message = "规格2不能为空")
    @TableField("spec_2")
    private BigDecimal spec2;

    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal specialUnitPrice;

    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    private String remark;

    @TableLogic
    private Byte isDeleted;

    private Integer printCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 封装客户名、工艺名、假单价
    @TableField(exist = false)
    private String customerName;

    @TableField(exist = false)
    private String processName;

    @TableField(exist = false)
    private BigDecimal unitPrice;

    // 封装打印模式、打印人
    @TableField(exist = false)
    private Integer mode;
}
