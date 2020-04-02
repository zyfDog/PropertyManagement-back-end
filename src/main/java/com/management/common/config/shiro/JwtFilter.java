package com.management.common.config.shiro;

import com.alibaba.fastjson.JSON;
import com.management.common.bean.ResponseResult;
import com.management.common.util.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @description: JWTFilter
 * @Auther: zyf
 * @Date: 2019-09-22
 * 代码的执行流程preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 **/
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Token");
        if (authorization != null && !"".equals(authorization.trim()) && authorization.matches("^[^.]+\\.[^.]+\\.[^.]+$")) {
            return true;
        }
        writerResponse(response, "缺少Token");
        return false;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        log.debug(">>> executeLogin执行");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Token");
        if (authorization == null || "".equals(authorization.trim())) {
            writerResponse(response, "未含授权标示，禁止访问");
        }
        JwtToken token = new JwtToken(authorization, Long.parseLong(Objects.requireNonNull(JwtUtil.get(authorization, "uid"))));
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return true;
        } catch (DisabledAccountException e) {
            if ("verifyFail".equals(e.getMessage())) {
                writerResponse(response, "身份已过期，请重新登录");
            }
            writerResponse(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            writerResponse(response, "身份已过期，请重新登录");
        }
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.debug("PermissionAuthorizationFilter执行");
        HttpServletRequest req = (HttpServletRequest) request;
        if (isLoginAttempt(request, response)) {
            log.debug(">>> Token" + req.getHeader("Token"));
            try {
                JwtToken token = new JwtToken(req.getHeader("Token"));
                getSubject(request, response).login(token);
                return true;
            } catch (Exception e) {
                writerResponse(response, "Token 无效");
            }
        }
        return false;
    }

    private void writerResponse(ServletResponse res, String content) {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(ResponseResult.builder()
                    .code(403)
                    .msg(content)
                    .build()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }

}
