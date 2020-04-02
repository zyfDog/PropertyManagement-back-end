package com.management.core.service.admin.complaint.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.common.bean.AdminCostResponseCode;
import com.management.common.util.RequestException;
import com.management.core.entity.Complaint;
import com.management.core.mapper.ComplaintMapper;
import com.management.core.service.admin.complaint.service.AdminComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminComplaintImpl implements AdminComplaintService {
    @Autowired
    ComplaintMapper complaintMapper;

    @Override
    public JSONObject getListByKeyword(String status, String date, String proprietorId) {
        QueryWrapper<Complaint> queryWrapper = new QueryWrapper<Complaint>();
        if (status != null && (!"".equals(status))) {
            if ("已处理".equals(status)) {
                queryWrapper.eq("status", 1);
            } else {
                queryWrapper.eq("status", 0);
            }
        }
        if (date != null && (!"".equals(date))) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ParsePosition pos = new ParsePosition(0);
            queryWrapper.eq("complain_date", formatter.parse(date, pos));
        }
        if (proprietorId != null && (!"".equals(proprietorId))) {
            queryWrapper.eq("proprietor_id", Long.parseLong(proprietorId));
        }
        return getList(queryWrapper);
    }


    @Override
    public JSONObject getList(QueryWrapper<Complaint> queryWrapper) {
        List<Complaint> complaints = complaintMapper.selectList(queryWrapper);
        JSONObject re = new JSONObject();
        re.put("total", complaints.size());
        JSONArray complaintList = new JSONArray();
        for (Complaint i : complaints
        ) {
            JSONObject complaintPojo = new JSONObject();
            complaintPojo.put("id", i.getId());
            complaintPojo.put("proprietorId", i.getProprietorId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = i.getComplainDate();
            if (date == null) {
                complaintPojo.put("complaintDate", "");
            } else {
                complaintPojo.put("complaintDate", simpleDateFormat.format(date));
            }
            complaintPojo.put("summary", i.getSummary());
            if (i.getStatus()) {
                complaintPojo.put("status", "已处理");
            } else {
                complaintPojo.put("status", "未处理");
            }
            Date conductDate = i.getConductDate();
            if (conductDate == null) {
                complaintPojo.put("dealDate", "");
            } else {
                complaintPojo.put("dealDate", simpleDateFormat.format(conductDate));
            }
            complaintList.add(complaintPojo);
        }
        re.put("list", complaintList);
        return re;
    }

    @Override
    public boolean insertComplaint(Complaint complaint) {
        return complaintMapper.insert(complaint) > 0;
    }

    @Override
    public boolean updateComplaint(Complaint complaint) {
        return complaintMapper.updateById(complaint) > 0;
    }

    @Override
    public boolean updateComplaintStatus(long id) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new RequestException(AdminCostResponseCode.COMPLAINT_ID_FAIL);
        }
        if (!complaint.getStatus()) {
            Date data = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(data);
            ParsePosition pos = new ParsePosition(0);
            complaint.setConductDate(formatter.parse(dateString, pos));
            complaint.setStatus(true);
        } else {
            complaint.setStatus(false);
        }
        return complaintMapper.updateById(complaint) > 0;
    }


}
