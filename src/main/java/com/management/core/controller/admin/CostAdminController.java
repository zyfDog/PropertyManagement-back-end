package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.AdminCostResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.common.util.JwtUtil;
import com.management.core.entity.Cost;
import com.management.core.service.admin.cost.service.AdminCostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/administrator/cost")
/**
 * @description: 缴费记录管理
 * @author: zyf
 * @create: 2019-09-25
 **/
public class CostAdminController {
    @Autowired
    AdminCostService adminCostService;

    @GetMapping("/list")
    public ResponseResult getList(HttpServletRequest request) {
        return ResponseResult.success(adminCostService.getList(null));
    }

    @GetMapping("/keyword")
    public ResponseResult getKeywordList(String status, String item, String proprietorId) {
        return ResponseResult.success(adminCostService.getListByKeyword(status, item, proprietorId));
    }

    @PostMapping
    public ResponseResult newCost(@RequestBody JSONObject cost, HttpServletRequest request) throws ParseException {
        Cost costPojo = new Cost();
        String uid = JwtUtil.get(request.getHeader("Token"), "uid");
        if (uid == null || "".equals(uid)) {
            return ResponseResult.e(AdminCostResponseCode.TOKEN_FAIL);
        }
        costPojo.setAuditorId(Long.valueOf(uid));
        costPojo.setProprietorId(cost.getLong("proprietorId"));
        costPojo.setItemId(adminCostService.getItemIdByValue(cost.getString("item")));
        Date date = new Date();
        //日期转时间戳（毫秒）
        long time = date.getTime();
        costPojo.setCostDate(time);
        costPojo.setMoney(cost.getDouble("money"));
        if (adminCostService.insertCost(costPojo)) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.e(AdminCostResponseCode.SQL_UNKNOW_FAIL);
        }
    }

    @PutMapping
    public ResponseResult editCost(@RequestBody JSONObject cost, HttpServletRequest request) throws ParseException {
        Cost costPojo = new Cost();
        String uid = JwtUtil.get(request.getHeader("Token"), "uid");
        if (uid == null || "".equals(uid)) {
            return ResponseResult.e(AdminCostResponseCode.TOKEN_FAIL);
        }
        costPojo.setId(cost.getLong("id"));
        costPojo.setProprietorId(cost.getLong("proprietorId"));
        costPojo.setItemId(adminCostService.getItemIdByValue(cost.getString("item")));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(cost.getString("costDate"));
        //日期转时间戳（毫秒）
        long time = date.getTime();
        costPojo.setCostDate(time);
        costPojo.setMoney(cost.getDouble("money"));
        if ("已缴费".equals(cost.getString("status"))) {
            costPojo.setStatus(true);
        } else {
            costPojo.setStatus(false);
        }
        if (adminCostService.updateCost(costPojo, Long.valueOf(uid))) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.e(AdminCostResponseCode.SQL_UNKNOW_FAIL);
        }
    }

    @PutMapping("/status")
    public ResponseResult editCostStatus(@RequestBody JSONObject cost, HttpServletRequest request) {
        String uid = JwtUtil.get(request.getHeader("Token"), "uid");
        if (uid == null || "".equals(uid)) {
            return ResponseResult.e(AdminCostResponseCode.TOKEN_FAIL);
        }
        Long costId = cost.getLong("id");
        if (costId == null || "".equals(costId)) {
            return ResponseResult.e(AdminCostResponseCode.COST_ID_FAIL);
        }
        if (adminCostService.updateCostStatus(costId, Long.valueOf(uid))) {
            return ResponseResult.success(null);
        } else {
            return ResponseResult.e(AdminCostResponseCode.SQL_UNKNOW_FAIL);
        }
    }

}
