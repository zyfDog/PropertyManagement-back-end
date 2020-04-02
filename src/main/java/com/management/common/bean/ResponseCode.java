package com.management.common.bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @Auther: zyf
 * @Date: 2019-09-18
 **/

@NoArgsConstructor
@AllArgsConstructor
public enum ResponseCode {
    /**
     * 返回登录状态
     */
    OK(200, "操作成功"),
    SIGN_IN_OK(200, "登录成功"),
    LOGOUT_OK(200, "注销登录成功"),
    SIGN_IN_INPUT_FAIL(-4, "账号或密码错误"),
    SIGN_IN_FAIL(-3, "登录失败"),
    FAIL(-1, "操作失败"),
    LOGOUT_FAIL(-2, "注销登录失败"),
    SING_IN_INPUT_EMPTY(-5, "账户和密码均不能为空"),
    NOT_SING_IN(-6, "用户未登录或身份异常"),
    NOT_AUTH_FAIL(403, "暂无权限");

    public Integer code;

    public String msg;

    public static List<ResponseMessage> getArrayMessage() {
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        for (ResponseCode statusEnum : ResponseCode.values()) {
            responseMessages.add(new ResponseMessageBuilder()
                    .code(statusEnum.code)
                    .message(statusEnum.msg)
                    .build());
        }
        return responseMessages;
    }

}
