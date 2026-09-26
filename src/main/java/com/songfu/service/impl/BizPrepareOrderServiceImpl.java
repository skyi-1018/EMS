package com.songfu.service.impl;

import com.songfu.mapper.BizOrderMapper;
import com.songfu.mapper.BizPrepareOrderMapper;
import com.songfu.pojo.BizOrder;
import com.songfu.dto.OrderABModeParam;
import com.songfu.pojo.BizPrepareOrder;
import com.songfu.service.BizOrderService;
import com.songfu.service.BizPrepareOrderService;
import com.songfu.service.BizPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BizPrepareOrderServiceImpl implements BizPrepareOrderService {

    @Autowired
    private BizPrepareOrderMapper bizPrepareOrderMapper;

    @Autowired
    private BizOrderService bizOrderService;

    @Autowired
    private BizPrintService printService;

    @Override
    public List<BizPrepareOrder> list() {
        return bizPrepareOrderMapper.list();
    }

    @Override
    public void batchDeleteByIds(List<Integer> ids) {
        bizPrepareOrderMapper.deleteByIds(ids);
    }

    @Override
    public void add(BizPrepareOrder bizPrepareOrder) {
        bizPrepareOrderMapper.insert(bizPrepareOrder);
    }

    @Override
    public void update(BizPrepareOrder bizPrepareOrder) {
        bizPrepareOrderMapper.updateById(bizPrepareOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer submit(BizOrder order) {
        bizPrepareOrderMapper.deleteById(order.getId());
        order.setId(null);
        return bizOrderService.add(order);
    }

    @Override
    public void ABModeAdd(OrderABModeParam orderABMode) {
        BizPrepareOrder orderA = new BizPrepareOrder();
        BizPrepareOrder orderB = new BizPrepareOrder();
        orderA.setCustomerId(orderABMode.getCustomerId());
        orderA.setProcessId(orderABMode.getProcessId());
        orderA.setOrderDate(orderABMode.getOrderDate());
        orderA.setProductName(orderABMode.getProductName() + "<A>");
        orderA.setSpec1(orderABMode.getSpec1A());
        orderA.setSpec2(orderABMode.getSpec2A());
        orderA.setSpecialUnitPrice(orderABMode.getSpecialUnitPrice());
        orderA.setRemark(orderABMode.getRemark());
        bizPrepareOrderMapper.insert(orderA);

        orderB.setCustomerId(orderABMode.getCustomerId());
        orderB.setProcessId(orderABMode.getProcessId());
        orderB.setOrderDate(orderABMode.getOrderDate());
        orderB.setProductName(orderABMode.getProductName() + "<B>");
        orderB.setSpec1(orderABMode.getSpec1B());
        orderB.setSpec2(orderABMode.getSpec2B());
        orderB.setSpecialUnitPrice(orderABMode.getSpecialUnitPrice());
        orderB.setRemark(orderABMode.getRemark());
        bizPrepareOrderMapper.insert(orderB);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAndPrint(BizOrder bizOrder) throws Exception {
        // 1.先调用本类的提交函数，并获取id
        Integer orderId = submit(bizOrder);

        // 2.组成List
        List<Integer> ids = new ArrayList<>();
        ids.add(orderId);

        // 3.调用打印service
        printService.printOrder(ids, bizOrder.getMode());
    }
}
