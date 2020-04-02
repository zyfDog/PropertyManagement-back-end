package com.management.common.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 返回响应
 * @Auther: zyf
 * @Date: 2019-09-22
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 8992436576262574064L;
    private final long timestamps = System.currentTimeMillis();
    private Integer code;
    private T data;
    private String msg;

    public synchronized static <T> ResponseResult<T> e(ResponseCode statusEnum) {
        return e(statusEnum, null);
    }

    public synchronized static <T> ResponseResult<T> e(com.management.common.util.ResponseCode statusEnum) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setCode(statusEnum.getCode());
        res.setMsg(statusEnum.getMsg());
        res.setData(null);
        return res;
    }

    public static ResponseResult e(Integer code, String msg) {
        return ResponseResult.builder().code(code).msg(msg).data(null).build();
    }

    public synchronized static <T> ResponseResult<T> e(ResponseCode statusEnum, T data) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setCode(statusEnum.code);
        res.setMsg(statusEnum.msg);
        res.setData(data);
        return res;
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setCode(200);
        res.setMsg("success");
        res.setData(data);
        return res;
    }

    public static <T> ResponseResult<T> err(T data, String msg) {
        ResponseResult<T> res = new ResponseResult<>();
        res.setCode(400);
        res.setMsg(msg);
        res.setData(data);
        return res;
    }
}
