package com.songfu.service.impl;

import com.songfu.mapper.BizOrderMapper;
import com.songfu.pojo.BizOrder;
import com.songfu.service.BizPrintService;
import com.songfu.utils.PrintUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BizPrintServiceImpl implements BizPrintService {

    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private PrintUtil printUtil;

    @Override
    public void printOrder(List<Integer> ids, Integer mode) throws Exception {

        List<BizOrder> orderList = bizOrderMapper.listByIds(ids);
        if (CollectionUtils.isEmpty(orderList)) {
            throw new RuntimeException("未查询到订单数据");
        }
        printUtil.printOrder(orderList, mode);

        // 打印次数加1
        orderList.forEach(order -> {
            log.info("打印次数+1，订单ID：{}", order.getId());
            order.setPrintCount(order.getPrintCount() + 1);
            bizOrderMapper.updateById(order);
        });
    }
}
