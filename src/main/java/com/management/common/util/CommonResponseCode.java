package com.management.common.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Auther: zyf
 * @Date: 2019-09-22
 */
@NoArgsConstructor
@AllArgsConstructor
public enum CommonResponseCode implements ResponseCode {
    /**
     * 返回登录状态
     */
    OK(200, "操作成功"),
    SIGN_IN_INPUT_FAIL(400, "账号或密码错误"),
    SIGN_IN_FAIL(400, "登录失败"),
    FAIL(400, "操作失败"),
    NOT_SING_IN(400, "用户未登录或身份异常"),
    UPLOAD_SUCCESS(200, "上传成功"),
    ILLEGAL_FORMAT(20000, "Illegal format");

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