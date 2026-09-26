package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("biz_prepare_order")
public class BizPrepareOrder {
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

    @TableField("spec_1")
    private BigDecimal spec1;

    @TableField("spec_2")
    private BigDecimal spec2;

    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal specialUnitPrice;

    @TableField(exist = false)
    private Integer quantity;

    @TableField(exist = false)
    private BigDecimal amount;

    private String remark;

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
}
