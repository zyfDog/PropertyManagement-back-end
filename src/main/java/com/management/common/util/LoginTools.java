package com.management.common.util;

import com.management.common.bean.ResponseCode;
import com.management.common.config.shiro.JwtToken;
import com.management.common.exception.RequestException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: zyf
 * @Date: 2019-09-22
 */
public class LoginTools {
    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 判断请求是否是Ajax
     *
     * @param request
     * @return
     */
    public static boolean ajax(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        return accept != null && accept.contains("application/json") || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest"));
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return attributes.getRequest();
    }

    public static PrincipalCollection getPrincipalCollection() {
        PrincipalCollection principalCollection = null;
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            principalCollection = subject.getPrincipals();
        }
        return principalCollection;
    }

    public static boolean executeLogin(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Token");
        if (authorization == null || "".equals(authorization.trim())) {
            throw RequestException.fail("未含授权标示，禁止访问");
        }
        JwtToken token = new JwtToken(authorization, Long.parseLong(JwtUtil.get(authorization, "uid")));
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (DisabledAccountException e) {
            if (e.getMessage().equals("verifyFail")) {
                throw new RequestException(ResponseCode.NOT_SING_IN.code, "身份已过期，请重新登录", e);
            }
            throw new RequestException(ResponseCode.SIGN_IN_INPUT_FAIL.code, e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RequestException(ResponseCode.SIGN_IN_FAIL, e);
        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    public static synchronized void executeLogin() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean b = LoginTools.executeLogin(request);
        if (!b) {
            throw RequestException.fail("身份已过期或无效，请重新认证");
        }
    }
}