package com.management.core.service.admin.pairs.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.Repairs;
import com.management.core.mapper.RepairsMapper;
import com.management.core.service.admin.dto.param.ReadRepairsListParam;
import com.management.core.service.admin.dto.param.UpdateRepairsListParam;
import com.management.core.service.admin.dto.resp.ReadRepairsListResp;
import com.management.core.service.admin.pairs.service.PairsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: zyf
 * @create: 2019-09-21
 **/

@Service
@Slf4j
public class PairsAdminServiceImpl implements PairsAdminService {

    @Autowired
    RepairsMapper repairsMapper;

    @Override
    public List<ReadRepairsListResp> getList(ReadRepairsListParam param) {
        List<ReadRepairsListResp> resps = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        QueryWrapper<Repairs> wrapper = new QueryWrapper<>();

        if (param.getId() != null) {
            wrapper.like("proprietor_id", param.getId());
        }

        if (param.getDate() != null && !param.getDate().isEmpty()) {
            try {
                wrapper.eq("repairs_time", dateFormat.parse(param.getDate()).getTime());
            } catch (ParseException e) {
                log.debug("err");
                return resps;
            }
        }

        if (param.getStatus() != null && !param.getStatus().isEmpty()) {
            int i;
            if ("未处理".equals(param.getStatus())) {
                i = 0;
                wrapper.eq("status", i);
            } else if ("已处理".equals(param.getStatus())) {
                i = 1;
                wrapper.eq("status", i);
            }
        }

        List<Repairs> repairsList = repairsMapper.selectList(wrapper);

        for (Repairs repairs : repairsList) {
            ReadRepairsListResp resp = new ReadRepairsListResp();

            resp.setSummary(repairs.getSummary());
            if (repairs.getStatus()) {
                resp.setStatus("已处理");
            } else {
                resp.setStatus("未处理");
            }
            resp.setServiceman(repairs.getServiceman());
            resp.setRepairDate(dateFormat.format(new Date(repairs.getRepairsTime())));
            resp.setServiceDate(dateFormat.format(new Date(repairs.getServiceDate())));
            resp.setProprietorId(repairs.getProprietorId());
            resp.setAddress(repairs.getAddress());
            resp.setId(repairs.getId());

            resps.add(resp);
        }
        return resps;
    }

    @Override
    public boolean updateItem(Long id, UpdateRepairsListParam param) {
        Repairs repairs = repairsMapper.selectById(id);

        if (repairs == null) {
            return false;
        }
        repairs.setSummary(param.getSummary());
        repairs.setAddress(param.getAddress());
        repairs.setServiceman(param.getServiceman());
        if ("已处理".equals(param.getStatus())) {
            repairs.setStatus(true);
        } else {
            repairs.setStatus(false);
        }

        if (repairsMapper.updateById(repairs) > 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteItem(Long id) {

        if (repairsMapper.deleteById(id) > 0) {
            return true;
        }
        return false;
    }
}