package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.AdminCostResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.core.entity.Complaint;
import com.management.core.service.admin.complaint.service.AdminComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/administrator/complaint")
/**
 * @description: 投诉记录管理
 * @author: zyf
 * @create: 2019-09-25
 **/
public class ComplaintAdminController {
    @Autowired
    AdminComplaintService adminComplaintService;

    @GetMapping("/keyword")
    public ResponseResult getKeywordList(String proprietorId, String date, String status) {

        return ResponseResult.success(adminComplaintService.getListByKeyword(status, date, proprietorId));

    }

    @GetMapping("/list")
    public ResponseResult getList(HttpServletRequest request) {
        return ResponseResult.success(adminComplaintService.getList(null));
    }

    @PostMapping
    public ResponseResult newCost(@RequestBody JSONObject complaint, HttpServletRequest request) throws ParseException {
        Complaint complaintPojo = new Complaint();
        complaintPojo.setProprietorId(complaint.getLong("proprietorId"));
        complaintPojo.setSummary(complaint.getString("summary"));
        complaintPojo.setStatus(false);
        Date data = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(data);
        ParsePosition pos = new ParsePosition(0);
        complaintPojo.setComplainDate(formatter.parse(dateString, pos));
        if (adminComplaintService.insertComplaint(complaintPojo)) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.e(AdminCostResponseCode.SQL_UNKNOW_FAIL);
        }
    }

    @PutMapping
    public ResponseResult editCost(@RequestBody JSONObject complaint, HttpServletRequest request) throws ParseException {
        Complaint complaintPojo = new Complaint();
        complaintPojo.setId(complaint.getLong("id"));
        complaintPojo.setProprietorId(complaint.getLong("proprietorId"));
        complaintPojo.setSummary(complaint.getString("summary"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        complaintPojo.setComplainDate(format.parse(complaint.getString("complaintDate")));
        complaintPojo.setConductDate(format.parse(complaint.getString("dealDate")));
        if ("已处理".equals(complaint.getString("status"))) {
            complaintPojo.setStatus(true);
        } else {
            complaintPojo.setStatus(false);
        }
        if (adminComplaintService.updateComplaint(complaintPojo)) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.e(AdminCostResponseCode.SQL_UNKNOW_FAIL);
        }
    }

    @PutMapping("/status")
    public ResponseResult editCostStatus(@RequestBody JSONObject complaint, HttpServletRequest request) {

        Long complaintId = complaint.getLong("id");
        if (complaintId == null || "".equals(complaintId)) {
            return ResponseResult.e(AdminCostResponseCode.COST_ID_FAIL);
        }
        if (adminComplaintService.updateComplaintStatus(complaintId)) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.e(AdminCostResponseCode.SQL_UNKNOW_FAIL);
        }
    }

}
