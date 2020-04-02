package com.management.core.service.admin.cost.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.Cost;
import org.springframework.stereotype.Service;

/**
 * @description: 报修管理业务接口
 * @author: zyf
 * @create: 2019-09-18
 **/
@Service
public interface AdminCostService {


    JSONObject getListByKeyword(String status, String item, String proprietorId);

    JSONObject getList(QueryWrapper<Cost> queryWrapper);


    boolean insertCost(Cost cost);

    boolean updateCost(Cost cost, long adminId);

    boolean updateCostStatus(long id, long adminId);

    long getItemIdByValue(String value);
}