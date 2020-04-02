package com.management.core.service.admin.complaint.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.Complaint;
import org.springframework.stereotype.Service;

/**
 * @description: 报修管理业务接口
 * @author: zyf
 * @create: 2019-09-18
 **/
@Service
public interface AdminComplaintService {

    JSONObject getListByKeyword(String status, String date, String proprietorId);

    JSONObject getList(QueryWrapper<Complaint> queryWrapper);

    boolean insertComplaint(Complaint complaint);

    boolean updateComplaint(Complaint complaint);

    boolean updateComplaintStatus(long id);
}