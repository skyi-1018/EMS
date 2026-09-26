package com.songfu.service;

import com.songfu.pojo.BizProcess;

import java.util.List;

public interface BizProcessService {
    /*
    * 工艺信息列表查询
    * */
    List<BizProcess> list();

    /*
    * 更新工艺*
    * */
    void update(BizProcess process);

    /*
    * 删除工艺*
    * */
    void deleteById(Integer id);

    /*
    * 新增工艺*
    * */
    void add(BizProcess process);
}
