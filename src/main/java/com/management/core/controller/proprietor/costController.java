package com.management.core.controller.proprietor;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.management.common.annotation.SysLogs;
import com.management.common.bean.ResponseResult;
import com.management.common.util.JwtUtil;
import com.management.core.entity.Cost;
import com.management.core.mapper.CostMapper;
import com.management.core.service.proprietor.cost.comm.AliPayKey;
import com.management.core.service.proprietor.cost.comm.CostComm;
import com.management.core.service.proprietor.cost.service.CostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class costController {
    @Resource
    private CostService costService;

    @Resource
    private CostMapper costMapper;

    /**
     * 调用支付宝支付接口
     *
     * @return 支付是否成功
     */
    @PostMapping("/aliPay/preCreate/{id}")
    @ResponseBody
    @SysLogs("调用支付宝支付接口")
    public ResponseResult getPayState(@PathVariable String id) throws Exception {
        Long cid = Long.parseLong(id);
        Cost cost = costMapper.selectById(cid);

        //判断是否有该缴费记录
        if (cost == null) {
            return ResponseResult.err(null, "无该缴费记录");
        }

        //判断是否缴过费用
        if (!cost.getInvoice().isEmpty() || cost.getStatus()) {
            return ResponseResult.err(null, "已缴费");
        }

        Long serialNumber = System.currentTimeMillis() + cid;

        //调用支付宝api
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                AliPayKey.getAppId(), AliPayKey.getPrivateKey(), "json", "GBK",
                AliPayKey.getPublicKey(), "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + serialNumber + "\"," +
                "\"total_amount\":" + cost.getMoney() + "," +
                "\"subject\":\"物业管理系统缴费\"" +
                "  }");
        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        //判断是否请求成功
        if (!response.isSuccess()) {
            return ResponseResult.err(response, "调用支付宝接口失败");
        }

        //封装返回信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", cid);
        jsonObject.put("serialNumber", serialNumber);
        jsonObject.put("qrCode", response.getQrCode());

        return ResponseResult.success(jsonObject);
    }

    @GetMapping("/aliPay/query/{id}")
    public ResponseResult queryPayState(@PathVariable String id, String serialNumber) throws Exception {

        Long cid = Long.parseLong(id);

        //调用支付宝接口查看支付状态
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                AliPayKey.getAppId(), AliPayKey.getPrivateKey(), "json", "GBK",
                AliPayKey.getPublicKey(), "RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + serialNumber + "\"" +
                "  }");
        AlipayTradeQueryResponse response = alipayClient.execute(request);

        //判断是否支付成功
        if (response != null && response.isSuccess() && "TRADE_SUCCESS".equals(response.getTradeStatus())) {
            Cost cost = costMapper.selectById(cid);
            if (cost == null) {
                return ResponseResult.err(response, "支付异常，请联系管理员");
            }

            cost.setInvoice(serialNumber);
            cost.setStatus(true);
            cost.setCostDate(System.currentTimeMillis());
            int num = costMapper.updateById(cost);
            if (num > 0) {
                return ResponseResult.success(null);
            } else {
                return ResponseResult.err(response, "数据未录入");
            }
        } else {
            return ResponseResult.err(response, "查询失败");
        }
    }

    /**
     * 根据业主id查询业主所有缴费信息
     *
     * @param request http
     * @return 缴费信息
     */
    @GetMapping("/proprietor/cost")
    public ResponseResult getAllCostRecordByProprietorId(HttpServletRequest request) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            List<CostComm> costComms = costService.getAllCostRecordByProprietorId(uid);
            return ResponseResult.success(costComms);
        } catch (Exception e) {
            return ResponseResult.err(null, "查询失败");
        }
    }

    /**
     * 根据业主id和日期查询业主的缴费信息
     *
     * @param request http
     * @param date    日期
     * @return 缴费信息
     */
    @GetMapping("/proprietor/cost/date")
    public ResponseResult getCostRecordByDate(HttpServletRequest request, String date) {
        try {
            Long uid = Long.parseLong(Objects.requireNonNull(JwtUtil.get(request.getHeader("Token"), "uid")));
            List<CostComm> costComms = costService.getCostRecordByDate(uid, date);
            return ResponseResult.success(costComms);
        } catch (Exception e) {
            return ResponseResult.err(null, "查询失败");
        }
    }


}
