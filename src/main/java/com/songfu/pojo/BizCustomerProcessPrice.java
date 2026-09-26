package com.songfu.pojo;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_customer_process_price")
public class BizCustomerProcessPrice {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "客户Id不得为空")
    private Integer customerId;

    @NotNull(message = "工艺Id不得为空")
    private Integer processId;

    @NotNull(message = "真单价不得为空")
    private BigDecimal realUnitPrice;

    @NotNull(message = "假单价不得为空")
    private BigDecimal fakeUnitPrice;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
