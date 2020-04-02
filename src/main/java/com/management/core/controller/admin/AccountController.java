package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.common.bean.ResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.common.config.shiro.JwtToken;
import com.management.common.exception.RequestException;
import com.management.common.util.JwtUtil;
import com.management.core.entity.SysUser;
import com.management.core.service.system.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @description: 账户相关控制器
 * @author: zyf
 * @create: 2019-10-01 14:25
 **/
@Slf4j
@RestController
public class AccountController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody JSONObject jsonObject) {

        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        if ("".equals(username) || "".equals(password)) {
            return ResponseResult.e(ResponseCode.SING_IN_INPUT_EMPTY);
        }
        SysUser user = userService.getOne(new QueryWrapper<SysUser>().eq("username", username));
        if (user == null) {
            return ResponseResult.e(ResponseCode.SIGN_IN_FAIL);
        }
        Long uid = user.getId();
        JwtToken jwtToken = new JwtToken(null, uid, username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(jwtToken);
            if (!subject.isAuthenticated()) {
                return ResponseResult.e(ResponseCode.SIGN_IN_INPUT_FAIL);
            }
        } catch (DisabledAccountException e) {
            return ResponseResult.e(ResponseCode.SIGN_IN_INPUT_FAIL.code, e.getMessage());
        } catch (Exception e) {
            return ResponseResult.e(ResponseCode.SIGN_IN_FAIL, e);
        }
        JwtToken token = (JwtToken) SecurityUtils.getSubject().getPrincipal();
        String redisValue = "JWTToken-Cache::" + token.getUid();

        redisTemplate.opsForValue()
                .set(redisValue, token.getToken());
        redisTemplate.expire(redisValue, 1L, TimeUnit.DAYS);

        JSONObject responseData = new JSONObject();
        responseData.put("Token", token.getToken());
        responseData.put("username", token.getUsername());
        String role = "";
        if (subject.hasRole("tourist")) {
            role = "业主";
        }
        if (subject.hasRole("admin") || subject.hasRole("administrator")) {
            role = "管理员";
        }
        responseData.put("role", role);
        return ResponseResult.e(ResponseCode.SIGN_IN_OK, responseData);
    }

    @GetMapping("/logout")
    public ResponseResult<String> logout(HttpServletRequest httpRequest) {
        Subject subject = SecurityUtils.getSubject();
        JwtToken token = new JwtToken(httpRequest.getHeader("Token"),
                Long.parseLong(Objects.requireNonNull(JwtUtil.get(httpRequest.getHeader("Token"), "uid"))));
        try {
            subject.login(token);
        } catch (Exception e) {
            throw new RequestException(ResponseCode.NOT_SING_IN.code, e.getMessage(), e);
        }

        String redisValue = "JWTToken-Cache-Blacklist::" + JwtUtil.get(token.getToken(), "uid");
        redisTemplate.opsForSet()
                .add(redisValue, token.getToken());
        redisTemplate.expire(redisValue, 10L, TimeUnit.DAYS);
        return ResponseResult.e(ResponseCode.OK);
    }


    @PostMapping("/register")
    @RequiresRoles("admin")
    public ResponseResult register(@RequestBody JSONObject usrJson) {
        String username = usrJson.getString("username");
        String password = usrJson.getString("password");
        if (username == null || password == null) {
            return ResponseResult.e(ResponseCode.FAIL);
        }
        if (userService.getOne(new QueryWrapper<SysUser>().eq("username", username)) != null) {
            return ResponseResult.builder().code(400).msg("账号已存在").build();
        }

        String pre = usrJson.getString("permission");

        int type = "管理员".equals(pre) ? 1 : 2;
        Subject subject = SecurityUtils.getSubject();
        if (type == 1 && !subject.hasRole("administrator")) {
            return ResponseResult.e(ResponseCode.NOT_AUTH_FAIL);
        }
        userService.register(username, password, type);
        return ResponseResult.e(ResponseCode.OK);
    }

}
