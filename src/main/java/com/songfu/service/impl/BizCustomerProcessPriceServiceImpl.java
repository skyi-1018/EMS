package com.songfu.service.impl;

import com.songfu.mapper.BizCustomerProcessPriceMapper;
import com.songfu.pojo.BizCustomerProcessPrice;
import com.songfu.service.BizCustomerProcessPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BizCustomerProcessPriceServiceImpl implements BizCustomerProcessPriceService {

    @Autowired
    private BizCustomerProcessPriceMapper bizCustomerProcessPriceMapper;

    @Override
    public Map<Integer, Map<String, BigDecimal>> getCustomerUnitPriceById(Integer customerId) {
        Map<Integer, Map<String, BigDecimal>> processPriceMap = new HashMap<>();
        List<BizCustomerProcessPrice> list = bizCustomerProcessPriceMapper.getCustomerUnitPriceById(customerId);
        list.forEach(item -> {
            Map<String, BigDecimal> map = new HashMap<>();
            map.put("realUnitPrice", item.getRealUnitPrice());
            map.put("fakeUnitPrice", item.getFakeUnitPrice());
            processPriceMap.put(item.getProcessId(), map);
        });
        return processPriceMap;
    }

    @Override
    public Map<Integer, Map<Integer, Map<String, BigDecimal>>> getCustomerFakeUnitPriceMap() {
        Map<Integer, Map<Integer, Map<String, BigDecimal>>> customerUnitPriceMap = new HashMap<>();
        List<BizCustomerProcessPrice> list = bizCustomerProcessPriceMapper.getCustomerFakeUnitPriceMap();
        list.forEach(item -> {
            Map<String, BigDecimal> submap = new HashMap<>();
            submap.put("fakeUnitPrice", item.getFakeUnitPrice());
            Map<Integer, Map<String, BigDecimal>> processMap = customerUnitPriceMap.computeIfAbsent(item.getCustomerId(), k -> new HashMap<>());
            processMap.put(item.getProcessId(), submap);
        });
        return customerUnitPriceMap;
    }

    @Override
    public void addCustomerUnitPriceMap(Integer customerId, Map<Integer, Map<String, BigDecimal>> customerUnitPriceMap) {
        if (!CollectionUtils.isEmpty(customerUnitPriceMap)) {
            List<BizCustomerProcessPrice> unitPriceList = new java.util.ArrayList<>();
            customerUnitPriceMap.forEach((key, value) -> {
                if (value.get("realUnitPrice") == null && value.get("fakeUnitPrice") == null) return;
                BizCustomerProcessPrice customerProcessPrice = new BizCustomerProcessPrice();
                customerProcessPrice.setCustomerId(customerId);
                customerProcessPrice.setProcessId(key);
                customerProcessPrice.setRealUnitPrice(value.get("realUnitPrice"));
                customerProcessPrice.setFakeUnitPrice(value.get("fakeUnitPrice"));
                unitPriceList.add(customerProcessPrice);
            });
            if (!CollectionUtils.isEmpty(unitPriceList)) bizCustomerProcessPriceMapper.add(unitPriceList);
        }
    }
}
