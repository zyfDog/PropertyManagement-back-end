package com.management.common.bean;

import com.management.common.util.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum AdminCostResponseCode implements ResponseCode {
    /**
     * 返回登录状态
     */
    OK(200, "操作成功"),
    SUCCESS(200, "操作成功"),
    INTERNAL_SERVER_ERROR(500, "非业务类异常"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "unauthorized"),
    BAD_REQUEST(400, "请求错误"),
    FAIL(400, "operation failed"),
    COST_ID_FAIL(20700, "缴费记录id异常，不存在此条记录"),
    COST_ITEM_FAIL(20701, "缴费类型异常"),
    SQL_UNKNOW_FAIL(20702, "数据库未知错误"),
    TOKEN_FAIL(20703, "token异常"),
    COMPLAINT_ID_FAIL(20704, "投诉记录id异常，不存在此条记录");
    public Integer code;
    public String msg;


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}