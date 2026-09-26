package com.songfu.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderKeywordSuggestion {
    private String productName;
    private BigDecimal spec1;
    private BigDecimal spec2;
}
