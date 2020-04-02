package com.management.core.controller.proprietor;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.ResponseResult;
import com.management.common.util.JwtUtil;
import com.management.core.service.proprietor.repairs.comm.RepairsComm;
import com.management.core.service.proprietor.repairs.service.RepairsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/proprietor/repairs")
public class repairsController {
    @Resource
    private RepairsService repairsService;

    /**
     * 根据业主id获取所有报修记录
     *
     * @param request http
     * @return 报修记录
     */
    @GetMapping("")
    public ResponseResult getAllRepairsRecord(HttpServletRequest request) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            List<RepairsComm> repairsCommList = repairsService.getAllRepairsRecord(uid);
            return ResponseResult.success(repairsCommList);
        } catch (Exception e) {
            return ResponseResult.err(null, "查询失败");
        }
    }

    /**
     * 根据业主id添加报修记录
     *
     * @param request    http
     * @param jsonObject 报修内容
     * @return 添加是否成功
     */
    @PostMapping("")
    public ResponseResult addRepairsRecord(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            boolean flag = repairsService.addRepairsRecord(uid, jsonObject.getString("summary"));
            return ResponseResult.success(flag);
        } catch (Exception e) {
            return ResponseResult.err(null, "添加失败");
        }
    }

    /**
     * 根据报修记录id修改报修记录
     *
     * @param jsonObject 记录id和报修信息
     * @return 修改是否成功
     */
    @PutMapping("")
    public ResponseResult updateRepairsRecord(@RequestBody JSONObject jsonObject) {
        try {
            boolean flag = repairsService.updateRepairsRecord(jsonObject.getLong("id"), jsonObject.getString("summary"));
            if (flag) {
                return ResponseResult.success(null);
            } else {
                return ResponseResult.err(null, "修改失败");
            }
        } catch (Exception e) {
            return ResponseResult.err(null, "修改失败");
        }
    }

    /**
     * 根据业主id和内容关键字查询报修记录
     *
     * @param request http
     * @param keyword 关键字
     * @return 报修记录
     */
    @GetMapping("/keyword")
    public ResponseResult getRepairsRecordByKeyword(HttpServletRequest request, String keyword) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            List<RepairsComm> result = repairsService.getRepairsRecordByKeyword(uid, keyword);
            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.err(null, "修改失败");
        }
    }
}
