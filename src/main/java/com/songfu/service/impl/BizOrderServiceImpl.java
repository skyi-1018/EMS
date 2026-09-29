package com.songfu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.songfu.common.UserContext;
import com.songfu.dto.OrderABModeParam;
import com.songfu.dto.OrderListParam;
import com.songfu.model.ExcelExportItem;
import com.songfu.vo.OrderKeywordSuggestion;
import com.songfu.mapper.BizCustomerMapper;
import com.songfu.mapper.BizOrderMapper;
import com.songfu.pojo.*;
import com.songfu.service.BizOrderService;
import com.songfu.service.BizPrintService;
import com.songfu.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class BizOrderServiceImpl implements BizOrderService {

    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private BizPrintService bizPrintService;

    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    @Override
    public PageResult<BizOrder> list(OrderListParam param) {
        //1.构造分页对象
        Page<BizOrder> page = new Page<>(param.getPageNum(), param.getPageSize());

        // 2.执行查询，把page作为第一个参数传给Mapper,查询结果直接写进page
        bizOrderMapper.list(page, param);

        // 3.解析结果并返回
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public void deleteById(Integer id) {
        bizOrderMapper.deleteById(id);
    }

    @Override
    public Integer add(BizOrder order) {
        bizOrderMapper.insert(order);
        return order.getId();
    }

    @Override
    public void update(BizOrder order) {
        BizOrder dbOrder = bizOrderMapper.selectById(order.getId());
        if (dbOrder.getPrintCount() > 0 && UserContext.getRole() == 1) throw new RuntimeException("已打印的订单禁止修改"); // 非管理员已打印订单禁止修改
        bizOrderMapper.updateById(order);
    }

    /*
    * 根据mode组装导出数据集合
    * 如果mode==1，item -> controller下载xlsx
    * 如果mode==2，item -> controller下载zip
    * */
    @Override
    public List<ExcelExportItem>  excelExport(Integer mode, YearMonth month, Integer customerId) {
        LocalDate beginDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        List<ExcelExportItem> itemList = new ArrayList<>();

        if (mode == 1) {
            // 打包为ZIP下载
            List<BizCustomer> customerList = bizCustomerMapper.list(null);
            for (BizCustomer customer : customerList) {
                List<BizOrder> orderList = bizOrderMapper.listForExcel(beginDate, endDate, customer.getId());
                if (CollectionUtils.isEmpty(orderList)) continue;
                itemList.add(new ExcelExportItem<>(month + " " + customer.getName(), orderList));
            }
            List<BizOrder> orderList = bizOrderMapper.listForExcel(beginDate, endDate, null);
            itemList.add(new ExcelExportItem<>(month.toString(), orderList));
        } else if (mode == 2) {
            // Excel 下载
            List<BizOrder> orderList = bizOrderMapper.listForExcel(beginDate, endDate, null);
            itemList.add(new ExcelExportItem<>(month.toString(), orderList));
        } else if (mode == 3) {
            // 指定客户Excel下载
            List<BizOrder> orderList = bizOrderMapper.listForExcel(beginDate, endDate, customerId);
            itemList.add(new ExcelExportItem<>(month + " " + bizCustomerMapper.selectById(customerId).getName(), orderList));
        } else {
            throw new RuntimeException("导出模式参数错误");
        }
        return itemList;
    }

    @Override
    public List<OrderKeywordSuggestion> keywordSuggest(OrderListParam param) {
        return bizOrderMapper.keywordSuggest(param);
    }

    @Override
    public List<Integer> ABModeAdd(OrderABModeParam orderABMode) {
        BizOrder orderA = new BizOrder();
        BizOrder orderB = new BizOrder();
        orderA.setCustomerId(orderABMode.getCustomerId());
        orderA.setProcessId(orderABMode.getProcessId());
        orderA.setOrderDate(orderABMode.getOrderDate());
        orderA.setProductName(orderABMode.getProductName() + "<A>");
        orderA.setSpec1(orderABMode.getSpec1A());
        orderA.setSpec2(orderABMode.getSpec2A());
        orderA.setQuantity(orderABMode.getQuantityA());
        orderA.setAmount(orderABMode.getAmountA());
        orderA.setSpecialUnitPrice(orderABMode.getSpecialUnitPrice());
        orderA.setRemark(orderABMode.getRemark());
        bizOrderMapper.insert(orderA);

        orderB.setCustomerId(orderABMode.getCustomerId());
        orderB.setProcessId(orderABMode.getProcessId());
        orderB.setOrderDate(orderABMode.getOrderDate());
        orderB.setProductName(orderABMode.getProductName() + "<B>");
        orderB.setSpec1(orderABMode.getSpec1B());
        orderB.setSpec2(orderABMode.getSpec2B());
        orderB.setQuantity(orderABMode.getQuantityB());
        orderB.setAmount(orderABMode.getAmountB());
        orderB.setSpecialUnitPrice(orderABMode.getSpecialUnitPrice());
        orderB.setRemark(orderABMode.getRemark());
        bizOrderMapper.insert(orderB);

        // 返回订单IDList
        List<Integer> ids = new ArrayList<>();
        ids.add(orderA.getId());
        ids.add(orderB.getId());
        return ids;
    }

    @Override
    public void ABModeSubmitAndPrint(OrderABModeParam orderABMode) throws Exception {
        // 1.先调用本类的ABModeAdd方法，将两个订单添加到数据库
        List<Integer> ids = ABModeAdd(orderABMode);

        // 2.再调用打印类方法
        bizPrintService.printOrder(ids, orderABMode.getMode());
    }

    @Override
    public void submitAndPrint(BizOrder order) throws Exception {
        // 1.先调用Mapper的add方法，将订单添加到数据库
        Integer orderId = add(order);
        List<Integer> ids = new ArrayList<>();
        ids.add(orderId);
        // 2.再调用打印类方法
        bizPrintService.printOrder(ids, order.getMode());
    }

    @Override
    public List<BizOrder> listDeletedOrders() {
        return bizOrderMapper.listDeletedOrders();
    }

    @Override
    public void restoreByIds(List<Integer> ids) {
        bizOrderMapper.restoreByIds(ids);
    }
}
