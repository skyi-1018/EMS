package com.songfu.service.impl;

import com.songfu.mapper.BizCustomerMapper;
import com.songfu.mapper.BizCustomerProcessPriceMapper;
import com.songfu.pojo.BizCustomer;
import com.songfu.pojo.BizCustomerProcessPrice;
import com.songfu.service.BizCustomerProcessPriceService;
import com.songfu.service.BizCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BizCustomerServiceImpl implements BizCustomerService {

    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    @Autowired
    private BizCustomerProcessPriceMapper bizCustomerProcessPriceMapper;

    @Autowired
    private BizCustomerProcessPriceService bizCustomerProcessPriceService;

    @Override
    public List<BizCustomer> list(String customerName) {
        List<BizCustomer> customerList = bizCustomerMapper.list(customerName);
        customerList.forEach(item -> {
            item.setUnitPriceMap(bizCustomerProcessPriceService.getCustomerUnitPriceById(item.getId()));
        });
        return customerList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByCustomerId(Integer customerId) {
        bizCustomerProcessPriceMapper.deleteByCustomerId(customerId);
        bizCustomerMapper.deleteById(customerId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(BizCustomer customer) {
        bizCustomerMapper.insert(customer);
        bizCustomerProcessPriceService.addCustomerUnitPriceMap(customer.getId(), customer.getUnitPriceMap());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(BizCustomer customer) {
        bizCustomerMapper.updateById(customer);
        bizCustomerProcessPriceMapper.deleteByCustomerId(customer.getId());
        bizCustomerProcessPriceService.addCustomerUnitPriceMap(customer.getId(), customer.getUnitPriceMap());
    }

    @Override
    public List<BizCustomer> simpleList() {
        return bizCustomerMapper.selectList(null);
    }
}
