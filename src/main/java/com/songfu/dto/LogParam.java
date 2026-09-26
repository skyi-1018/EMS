package com.songfu.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogParam {
    private Integer userId;
    private String operation;
    private String ip;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
