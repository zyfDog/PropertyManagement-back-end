package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.ResponseResult;
import com.management.common.exception.RequestException;
import com.management.core.service.admin.dto.param.ReadRepairsListParam;
import com.management.core.service.admin.dto.param.UpdateRepairsListParam;
import com.management.core.service.admin.dto.resp.ReadRepairsListResp;
import com.management.core.service.admin.pairs.service.PairsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 报修管理
 * @author: zyf
 * @create: 2019-09-25
 **/

@Slf4j
@RestController
@RequestMapping("/administrator/repairs")
public class RepairsAdminController {

    @Autowired
    PairsAdminService pairsAdminService;

    @GetMapping("/list")
    public ResponseResult readPairsList(Long id, String status, String date) {

        ReadRepairsListParam param = ReadRepairsListParam.builder()
                .id(id)
                .date(date)
                .status(status)
                .build();


        List<ReadRepairsListResp> listResps = pairsAdminService.getList(param);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", listResps.size());
        jsonObject.put("list", listResps);

        return ResponseResult.success(jsonObject);

    }

    @PutMapping("/{id}")
    public ResponseResult updateRepairs(@PathVariable Long id, @RequestBody UpdateRepairsListParam param) {

        boolean flag = pairsAdminService.updateItem(id, param);

        if (flag) {
            return ResponseResult.success(new JSONObject());
        } else {
            throw RequestException
                    .fail("put /administrator/repairs");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseResult deletePairs(@PathVariable Long id) {


        boolean flag = pairsAdminService.deleteItem(id);
        if (flag) {
            return ResponseResult.success(new JSONObject());
        } else {
            throw RequestException
                    .fail("delete /administrator/repairs");
        }
    }

}