package com.songfu.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommonUtil {

    /*
    * 获取所有日期列表
    * mode: 1-月度, 2-年度
    * startDate: 开始日期(包含)
    * endDate: 结束日期(不包含)
    * */
    public List<String> getAllDateList(int mode, LocalDate startDate, LocalDate endDate) {
        List<String> dateList = new ArrayList<>();
        LocalDate current = startDate;
        if (mode == 1) {
            while (current.isBefore(endDate)) {
                dateList.add(current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                current = current.plusDays(1);
            }
        } else if (mode == 2) {
            while (current.isBefore(endDate)) {
                dateList.add(current.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                current = current.plusMonths(1);
            }
        }
        return dateList;
    }
}
