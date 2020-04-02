package com.management.core.service.proprietor.proprietorAbout.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.CarportRecord;
import com.management.core.entity.Complaint;
import com.management.core.entity.Proprietor;
import com.management.core.entity.SysUser;
import com.management.core.mapper.CarportRecordMapper;
import com.management.core.mapper.ComplaintMapper;
import com.management.core.mapper.ProprietorMapper;
import com.management.core.mapper.SysUserMapper;
import com.management.core.service.proprietor.proprietorAbout.service.proprietorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha384Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class proprietorImpl implements proprietorService {
    @Autowired
    ProprietorMapper proprietorMapper;
    @Autowired
    CarportRecordMapper carportRecordMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    ComplaintMapper complaintMapper;

    @Override
    public JSONObject getProprietor(long id) {
        Proprietor proprietor = proprietorMapper.selectOne(new QueryWrapper<Proprietor>()
                .eq("proprietor_id", id));

        List<CarportRecord> carportRecords = carportRecordMapper.selectList(new QueryWrapper<CarportRecord>()
                .eq("proprietor_id", id));

        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("id", id));
        JSONObject jsonObject = new JSONObject();
        JSONObject account = new JSONObject();
        JSONObject proprietors = new JSONObject();
        JSONObject carport = new JSONObject();
        if (proprietor != null && sysUser != null) {
            account.put("username", sysUser.getUsername());
            account.put("password", sysUser.getPassword());
            account.put("realName", proprietor.getRealName());
            account.put("permission", sysUser.getType());
        }

        jsonObject.put("account", account);


        proprietors.put("id", id);
        if (proprietor != null) {
            proprietors.put("phone", proprietor.getPhone());
            proprietors.put("realName", proprietor.getRealName());
            proprietors.put("houseNumber", proprietor.getHouseNumber());
            proprietors.put("address", proprietor.getAddress());
        }
        jsonObject.put("proprietor", proprietors);
        if (carportRecords != null) {
            if (carportRecords.size() != 0) {
                carport.put("id", carportRecords.get(0).getCarportId());
                List<String> a = new LinkedList();
                for (CarportRecord i : carportRecords) {
                    DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String time = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(i.getStopStartDate()), ZoneId.systemDefault()));
                    a.add(time);
                    String time1 = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(i.getStopEndDate()), ZoneId.systemDefault()));
                    a.add(time1);
                    break;
                }
                carport.put("stopDate", a);
                if (carportRecords.get(0).getStatus()) {
                    carport.put("status", "停车中");
                } else {
                    carport.put("status", "车位为空");
                }


            }

        }

        jsonObject.put("carport", carport);

        return jsonObject;
    }

    /**
     * @Description: 修改账户信息
     * @Param:
     * @return:
     * @Author: WPX
     * @Date: 2019-7-17
     */
    @Override
    public boolean updateProprietor(long id, String password) {
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("id", id));
        sysUser.setPassword(new Sha384Hash(password + sysUser.getUsername()).toBase64());
        return sysUserMapper.updateById(sysUser) == 1;

    }

    @Override
    public boolean updateAccount(long id, String name, String phone, String number, String address) {
        Proprietor proprietor = proprietorMapper.selectOne(new QueryWrapper<Proprietor>()
                .eq("proprietor_id", id));
        proprietor.setRealName(name);
        proprietor.setPhone(phone);
        proprietor.setHouseNumber(number);
        proprietor.setAddress(address);
        return proprietorMapper.updateById(proprietor) == 1;

    }

    @Override
    public JSONObject getComplaintList(long id) {
        List<Complaint> complaintList = complaintMapper.selectList(new QueryWrapper<Complaint>()
                .eq("proprietor_id", id));
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Complaint i : complaintList) {
            JSONObject o = new JSONObject();
            o.put("id", i.getId());
            o.put("complaintDate", i.getComplainDate());
            o.put("summary", i.getSummary());
            o.put("dealDate", i.getConductDate());
            if (i.getStatus()) {
                o.put("status", "已处理");
            } else {
                o.put("status", "未处理");
            }
            jsonObjectList.add(o);
        }
        JSONObject result = new JSONObject();
        result.put("total", jsonObjectList.size());
        result.put("list", jsonObjectList);
        return result;
    }
}
