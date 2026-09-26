package com.songfu.service;

import java.io.IOException;
import java.util.List;

public interface BizPrintService {
    /*
    * 根据ids打印订单
    * */
    void printOrder(List<Integer> ids, Integer mode) throws Exception;
}
