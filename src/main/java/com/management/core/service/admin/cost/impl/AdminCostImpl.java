package com.management.core.service.admin.cost.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.common.bean.AdminCostResponseCode;
import com.management.common.util.RequestException;
import com.management.core.entity.Cost;
import com.management.core.entity.CostItem;
import com.management.core.mapper.CostItemMapper;
import com.management.core.mapper.CostMapper;
import com.management.core.service.admin.cost.service.AdminCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminCostImpl implements AdminCostService {
    @Autowired
    CostMapper costMapper;
    @Autowired
    CostItemMapper costItemMapper;

    @Override
    public JSONObject getListByKeyword(String status, String item, String proprietorId) {
        QueryWrapper<Cost> costQueryWrapper = new QueryWrapper<Cost>();
        if (status != null && (!"".equals(status))) {
            if ("已缴费".equals(status)) {
                costQueryWrapper.eq("status", 1);
            } else {
                costQueryWrapper.eq("status", 0);
            }
        }
        if (item != null && (!"".equals(item))) {
            costQueryWrapper.eq("item_id", getItemIdByValue(item));
        }
        if (proprietorId != null && (!"".equals(proprietorId))) {
            costQueryWrapper.eq("proprietor_id", Long.parseLong(proprietorId));
        }
        return getList(costQueryWrapper);
    }

    @Override
    public JSONObject getList(QueryWrapper<Cost> queryWrapper) {

        List<Cost> costs = costMapper.selectList(queryWrapper);
        JSONObject re = new JSONObject();
        re.put("total", costs.size());
        JSONArray costList = new JSONArray();
        for (Cost i : costs
        ) {
            JSONObject costPojo = new JSONObject();
            costPojo.put("id", i.getId());
            costPojo.put("proprietorId", i.getProprietorId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(i.getCostDate());
            costPojo.put("costDate", simpleDateFormat.format(date));
            costPojo.put("money", i.getMoney());
            costPojo.put("item", costItemMapper.selectById(i.getItemId()).getName());
            if (i.getStatus()) {
                costPojo.put("status", "已缴费");
            } else {
                costPojo.put("status", "未缴费");
            }
            costList.add(costPojo);
        }
        re.put("list", costList);
        return re;
    }


    @Override
    public boolean insertCost(Cost cost) {
        return costMapper.insert(cost) > 0;
    }

    @Override
    public boolean updateCost(Cost cost, long adminId) {
        cost.setAuditorId(adminId);
        return costMapper.updateById(cost) > 0;
    }

    @Override
    public boolean updateCostStatus(long id, long adminId) {
        Cost cost = costMapper.selectById(id);
        if (cost == null) {
            throw new RequestException(AdminCostResponseCode.COST_ID_FAIL);
        }
        cost.setAuditorId(adminId);
        if (!cost.getStatus()) {
            cost.setStatus(true);
        } else {
            cost.setStatus(false);
        }
        return costMapper.updateById(cost) > 0;
    }

    @Override
    public long getItemIdByValue(String value) {
        if (value == null || "".equals(value)) {
            throw new RequestException(AdminCostResponseCode.COST_ITEM_FAIL);
        }
        CostItem costItem = costItemMapper.selectOne(new QueryWrapper<CostItem>()
                .eq("name", value));
        if (costItem == null) {
            throw new RequestException(AdminCostResponseCode.COST_ITEM_FAIL);
        }
        return costItem.getId();
    }

}
