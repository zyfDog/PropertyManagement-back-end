package com.management.core.controller.proprietor;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.ResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.common.util.JwtUtil;
import com.management.core.service.proprietor.proprietorAbout.service.proprietorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 业主模块api
 * @author: zyf
 * @create: 2019-09-25
 */

@Slf4j
@RestController
public class ProprietorController {
    @Autowired
    proprietorService proprietorService;

    @GetMapping("/proprietor")
    public ResponseResult<Object> getProprietor(HttpServletRequest request) {

        long uid = Long.parseLong(JwtUtil.get(request.getHeader("Token"), "uid"));
        JSONObject jsonObject1 = proprietorService.getProprietor(uid);
        return ResponseResult.e(ResponseCode.OK, jsonObject1);

    }

    @PutMapping("/proprietor/password")
    public ResponseResult<Object> updatePassword(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        if (jsonObject.get("password") == null) {
            return ResponseResult.e(ResponseCode.FAIL, "密码为空");
        }
        String passwords = jsonObject.get("password").toString();
        long uid = Long.parseLong(JwtUtil.get(request.getHeader("Token"), "uid"));
        if (proprietorService.updateProprietor(uid, passwords)) {
            return ResponseResult.e(ResponseCode.OK);
        } else {
            return ResponseResult.e(ResponseCode.FAIL, "密码更新失败");
        }


    }

    @PutMapping("/proprietor")

    public ResponseResult<Object> updateAccount(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        if (jsonObject.get("phone") == null || jsonObject.get("name") == null ||
                jsonObject.get("number") == null || jsonObject.get("address") == null) {
            return ResponseResult.e(ResponseCode.FAIL, "boday数据缺失");
        }
        long uid = Long.parseLong(JwtUtil.get(request.getHeader("Token"), "uid"));
        String phone = jsonObject.get("phone").toString();
        String name = jsonObject.get("name").toString();
        String number = jsonObject.get("number").toString();
        String address = jsonObject.get("address").toString();

        if (proprietorService.updateAccount(uid, name, phone, number, address)) {
            return ResponseResult.e(ResponseCode.OK);
        } else {
            return ResponseResult.e(ResponseCode.FAIL, "更改业主信息失败");
        }

    }

    @GetMapping("/proprietor/complaints")

    public ResponseResult<Object> getComplaint(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        long uid = Long.parseLong(JwtUtil.get(request.getHeader("Token"), "uid"));
        return ResponseResult.e(ResponseCode.OK, proprietorService.getComplaintList(uid));


    }

}
