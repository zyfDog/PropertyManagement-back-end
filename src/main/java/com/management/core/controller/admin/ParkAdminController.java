package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.ResponseResult;
import com.management.common.exception.RequestException;
import com.management.core.service.admin.dto.param.CreateParkParam;
import com.management.core.service.admin.dto.param.ReadParkListParam;
import com.management.core.service.admin.dto.resp.ReadParkListResp;
import com.management.core.service.admin.park.service.ParkAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 停车场管理
 * @author: zyf
 * @create: 2019-09-25
 **/

@Slf4j
@RestController
@RequestMapping("/administrator/park")
public class ParkAdminController {

    @Autowired
    ParkAdminService parkService;

    @GetMapping("/list")
    public ResponseResult readParkList(Long id, String num, String status) {

        ReadParkListParam param = ReadParkListParam.builder()
                .id(id)
                .num(num)
                .status(status)
                .build();

        List<ReadParkListResp> listResps = parkService.getList(param);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", listResps.size());
        jsonObject.put("list", listResps);

        return ResponseResult.success(jsonObject);
    }

    @PutMapping("/{id}")
    public ResponseResult updatePark(@PathVariable Long id, @RequestBody JSONObject status) {
        boolean flag = parkService.updateItem(id, status.getString("status"));

        if (flag) {
            return ResponseResult.success(new JSONObject());
        } else {
            throw RequestException
                    .fail("put /administrator/park");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseResult deletePark(@PathVariable Long id) {

        boolean flag = parkService.deleteItem(id);
        if (flag) {
            return ResponseResult.success(new JSONObject());
        } else {
            throw RequestException
                    .fail("delete /administrator/park");
        }
    }

    @PostMapping
    public ResponseResult createPark(@RequestBody CreateParkParam parkParam) {

        boolean flag = parkService.createItem(parkParam);
        if (flag) {
            return ResponseResult.success(new JSONObject());
        } else {
            throw RequestException
                    .fail("post /administrator/park");
        }
    }

}