package com.management.core.controller.proprietor;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.ResponseResult;
import com.management.common.util.JwtUtil;
import com.management.core.service.proprietor.complaint.comm.ComplaintComm;
import com.management.core.service.proprietor.complaint.service.ComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/proprietor/complaint")
public class ComplaintController {
    @Resource
    private ComplaintService complaintService;

    @GetMapping("")
    public ResponseResult getAllComplaintByProprietorId(HttpServletRequest request) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            List<ComplaintComm> complaintComms = complaintService.getAllComplaintByProprietorId(uid);
            return ResponseResult.success(complaintComms);
        } catch (Exception e) {
            return ResponseResult.err(null, "查询失败");
        }
    }

    @GetMapping("/date")
    public ResponseResult getComplaintRecordBydate(HttpServletRequest request, String date) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            List<ComplaintComm> complaintComms = complaintService.getComplaintRecordBydate(uid, date);
            return ResponseResult.success(complaintComms);
        } catch (Exception e) {
            return ResponseResult.err(null, "查询失败");
        }
    }

    @PutMapping("/{id}")
    public ResponseResult updateComplaintByComplaintId(@PathVariable String id, @RequestBody JSONObject jsonObject) {
        try {
            Long uid = Long.parseLong(id);
            boolean flag = complaintService.updateComplaintByComplaintId(uid, jsonObject.getString("summary"));
            if (flag) {
                return ResponseResult.success(null);
            } else {
                return ResponseResult.err(null, "修改失败");
            }
        } catch (Exception e) {
            return ResponseResult.err(null, "修改失败");
        }
    }

    @PostMapping("")
    public ResponseResult addComplaintRecordByProprietorId(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            boolean flag = complaintService.addComplaintRecordByProprietorId(uid, jsonObject.getString("summary"));
            if (flag) {
                return ResponseResult.success(null);
            } else {
                return ResponseResult.err(null, "添加失败");
            }
        } catch (Exception e) {
            return ResponseResult.err(null, "添加失败");
        }
    }
}
