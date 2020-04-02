package com.management.core.service.admin.park.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.CarportInformation;
import com.management.core.entity.CarportRecord;
import com.management.core.entity.Cost;
import com.management.core.mapper.CarportInformationMapper;
import com.management.core.mapper.CarportRecordMapper;
import com.management.core.mapper.CostMapper;
import com.management.core.service.admin.dto.param.CreateParkParam;
import com.management.core.service.admin.dto.param.ReadParkListParam;
import com.management.core.service.admin.dto.resp.ReadParkListResp;
import com.management.core.service.admin.park.service.ParkAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: zyf
 * @create: 2019-09-22
 **/

@Service
@Slf4j
public class ParkAdminServiceImpl implements ParkAdminService {

    @Autowired
    CarportRecordMapper carportRecordMapper;

    @Autowired
    CarportInformationMapper carportInformationMapper;

    @Autowired
    CostMapper costMapper;

    @Override
    public List<ReadParkListResp> getList(ReadParkListParam param) {
        List<ReadParkListResp> resps = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        QueryWrapper<CarportRecord> wrapper = new QueryWrapper<>();

        if (param.getId() != null) {
            wrapper.like("proprietor_id", param.getId());
        }
        if (param.getNum() != null && !param.getNum().isEmpty()) {
            wrapper.like("car_number", param.getNum());
        }

        if (param.getStatus() != null && !param.getStatus().isEmpty()) {
            int i;
            if ("停车中".equals(param.getStatus())) {
                i = 1;
                wrapper.eq("status", i);
            } else if ("空闲".equals(param.getStatus())) {
                i = 0;
                wrapper.eq("status", i);
            } else {
                wrapper.eq("status", 3);
            }
        }

        List<CarportRecord> parkList = carportRecordMapper.selectList(wrapper);

        for (CarportRecord record : parkList) {
            ReadParkListResp resp = new ReadParkListResp();

            resp.setId(record.getCarportId());
            if (record.getStatus()) {
                resp.setStatus("停车中");
            } else {
                resp.setStatus("空闲");
            }
            resp.setCarNum(record.getCarNumber());
            resp.setAmount(record.getMoney());
            resp.setProprietorId(record.getProprietorId());
            resp.setUsePeriod(new ArrayList<>(Arrays.asList(dateFormat.format(new Date(record.getStopStartDate())),
                    dateFormat.format(new Date(record.getStopEndDate())))));

            resps.add(resp);
        }
        return resps;
    }

    @Override
    public boolean updateItem(Long id, String status) {
        QueryWrapper<CarportRecord> wrapper = new QueryWrapper<>();

        wrapper.eq("carport_id", id);


        CarportRecord record = carportRecordMapper.selectOne(wrapper);

        if (record == null) {
            return false;
        }
        if ("停车中".equals(status)) {
            record.setStatus(true);
        } else if ("空闲".equals(status)) {
            record.setStatus(false);
        } else {
            return false;
        }

        if (carportRecordMapper.updateById(record) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteItem(Long id) {
        QueryWrapper<CarportRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("carport_id", id);
        if (carportRecordMapper.delete(wrapper) > 0) {
            CarportInformation information = carportInformationMapper.selectById(id);
            if (information == null) {
                return false;
            }
            information.setStatus(false);
            if (carportInformationMapper.updateById(information) > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean createItem(CreateParkParam param) {
        Long time = 24 * 60 * 60 * 1000L;
        QueryWrapper<CarportInformation> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        List<CarportInformation> informationList = carportInformationMapper.selectList(wrapper);
        if (informationList.size() == 0) {
            return false;
        }

        for (CarportInformation information : informationList) {
            if (!information.getStatus()) {
                information.setStatus(true);
                if (carportInformationMapper.updateById(information) > 0) {
                    CarportRecord carportRecord = new CarportRecord();
                    carportRecord.setCarNumber(param.getCarNum());
                    carportRecord.setCarportId(information.getId());
                    carportRecord.setMoney(param.getDays() * 20.0);
                    carportRecord.setProprietorId(param.getId());
                    carportRecord.setStopStartDate(System.currentTimeMillis());
                    carportRecord.setStopEndDate((time * param.getDays()) + carportRecord.getStopStartDate());
                    if (carportRecordMapper.insert(carportRecord) > 0) {
                        Cost cost = new Cost();
                        cost.setItemId(4L);
                        cost.setProprietorId(param.getId());
                        System.out.println(param.getId());
                        cost.setMoney(param.getDays() * 20.0);
                        costMapper.insert(cost);
                        return true;
                    }
                }
            }
        }

        return false;
    }
}