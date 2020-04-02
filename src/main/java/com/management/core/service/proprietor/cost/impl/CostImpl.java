package com.management.core.service.proprietor.cost.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.Cost;
import com.management.core.entity.CostItem;
import com.management.core.mapper.CostItemMapper;
import com.management.core.mapper.CostMapper;
import com.management.core.service.proprietor.cost.comm.CostComm;
import com.management.core.service.proprietor.cost.service.CostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@Service
public class CostImpl implements CostService {
    @Resource
    private CostMapper costMapper;

    @Resource
    private CostItemMapper costItemMapper;

    @Override
    public List<CostComm> getAllCostRecordByProprietorId(Long id) {
        List<Cost> costs = costMapper.selectList(new QueryWrapper<Cost>()
                .eq("proprietor_id", id)
        );

        List<CostComm> costComms = new LinkedList<>();
        for (Cost cost : costs) {
            CostComm costComm = reloadCostCommByCost(cost);
            costComms.add(costComm);
        }
        return costComms;
    }

    @Override
    public List<CostComm> getCostRecordByDate(Long id, String date) {
        try {
            List<Cost> costs = costMapper.selectList(new QueryWrapper<Cost>()
                    .eq("proprietor_id", id)
            );

            List<CostComm> costComms = new LinkedList<>();
            for (Cost cost : costs) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                if (cost.getCostDate() != -1) {
                    String castDate = format.format(cost.getCostDate());
                    if (date.trim().equals(castDate)) {
                        CostComm costComm = reloadCostCommByCost(cost);
                        costComms.add(costComm);
                    }
                }
            }

            return costComms;
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    private CostComm reloadCostCommByCost(Cost cost) {
        CostComm costComm = new CostComm();
        costComm.setId(cost.getId());
        costComm.setCostDate(getLongTimeToStringTime(cost.getCostDate()));
        costComm.setMoney(cost.getMoney());

        if (cost.getItemId() != -1) {
            CostItem costItem = costItemMapper.selectById(cost.getItemId());
            costComm.setItem(costItem.getName());
        }

        if (cost.getStatus()) {
            costComm.setStatus("已缴费");
        } else {
            costComm.setStatus("未缴费");
        }

        return costComm;
    }

    private String getLongTimeToStringTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }
}
