package com.management.common.handler;

import com.management.common.bean.ResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.common.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @description: 全局异常
 * @Auther: zyf
 * @Date: 2019-09-22
 **/
@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(value = RequestException.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(RequestException e) {
        return ResponseResult.builder().msg(e.getMsg()).code(e.getStatus()).build();
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(DataIntegrityViolationException e) {
        return ResponseResult.builder().msg("数据操作格式异常").code(ResponseCode.FAIL.code).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String s = "参数验证失败";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return ResponseResult.builder().code(ResponseCode.FAIL.code).msg(s).build();
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public ResponseResult UnauthorizedExceptionHandler(UnauthorizedException e) {
        return ResponseResult.builder().msg(ResponseCode.NOT_AUTH_FAIL.msg).code(ResponseCode.NOT_AUTH_FAIL.code).data(ResponseCode.NOT_AUTH_FAIL.msg).build();
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseBody
    public ResponseResult UnauthenticatedExceptionHandler(UnauthenticatedException e) {
        return ResponseResult.builder().msg(ResponseCode.NOT_AUTH_FAIL.msg).code(ResponseCode.NOT_AUTH_FAIL.code).data(ResponseCode.NOT_AUTH_FAIL.msg).build();
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseResult nullPointerExceptionHandler(NullPointerException e) {
        e.printStackTrace();
        return ResponseResult.builder().msg("请求数据出错").code(ResponseCode.FAIL.code).build();
    }

    @ExceptionHandler(value = DisabledAccountException.class)
    @ResponseBody
    public ResponseResult DisabledAccountExceptionHandler(DisabledAccountException e) {
        return ResponseResult.builder().msg(e.getMessage()).code(ResponseCode.NOT_AUTH_FAIL.code).build();
    }
//
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public ResponseResult requestExceptionHandler(Exception e) {
//        return ResponseResult.builder().msg("服务器飘了，管理员去拿刀修理了~").code(ResponseCode.FAIL.code).build();
//    }

}
