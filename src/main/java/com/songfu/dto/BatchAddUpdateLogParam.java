package com.songfu.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BatchAddUpdateLogParam {
    private String version;
    private LocalDate updateDate;
    private List<String> updateContents;
}
