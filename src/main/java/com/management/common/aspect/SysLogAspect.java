package com.management.common.aspect;

import com.management.common.annotation.SysLogs;
import com.management.common.config.shiro.JwtToken;
import com.management.common.util.JwtUtil;
import com.management.common.util.LoginTools;
import com.management.core.entity.SysLog;
import com.management.core.mapper.SysLogMapper;
import org.apache.shiro.subject.PrincipalCollection;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Auther: zyf
 * @Date: 2019-09-25
 * @Description:
 */

//AOP日志
@Aspect
@Component
public class SysLogAspect {
    private Long startTime, endTime;

    private SysLog sysLog = new SysLog();

    @Resource
    private SysLogMapper sysLogMapper;

    static void setToLog(PrincipalCollection principalCollection, HttpServletRequest request, SysLog log) {
        log.setIp(LoginTools.getClientIp(request));
        log.setUri(request.getRequestURI());
        log.setHttpMethod(request.getMethod());

        if (principalCollection != null) {
            JwtToken token = (JwtToken) principalCollection.getPrimaryPrincipal();
            log.setUid(Optional.ofNullable(JwtUtil.get(token.getToken(), "uid")).orElse("0"));
        }
    }

    //统一切点,对com.management.common.annotation.SysLogs及其子包中所有的类的所有方法切面
    @Pointcut("@annotation(com.management.common.annotation.SysLogs)")
    public void sysLogs() {
    }

    //前置通知
    @Before("sysLogs()")
    public void doBefore(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
        // 获取 授权凭证, HTTP 请求头
        PrincipalCollection principalCollection = LoginTools.getPrincipalCollection();
        HttpServletRequest request = LoginTools.getRequest();
        sysLog.setActionName(getMethodSysLogAnnotationValue(joinPoint));
        // 设置 IP URL HTTP请求头 UID
        setToLog(principalCollection, request, sysLog);

        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                params.append("  ").append(paramNames[i]).append(": ").append(args[i]);
            }
            sysLog.setParams(params.toString());
        }
        // 请求的类
        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    //@AfterRunning: 返回通知 rsult为返回内容
    @AfterReturning(value = "sysLogs()")
    public void doAfter() {
        // 设置响应时间
        endTime = System.currentTimeMillis();
        sysLog.setResponseTimes(endTime - startTime);
        sysLogMapper.insert(sysLog);
    }

    //@AfterThrowing: 异常通知
    @AfterThrowing(value = "sysLogs()", throwing = "exception")
    public void doAfterThrowing(Throwable exception) {
        // 响应时间
        endTime = System.currentTimeMillis();
        sysLog.setResponseTimes(endTime - startTime);
        // 异常名称
        String exceptionName = exception.getStackTrace()[0].getClassName();
        sysLog.setThrowing(exceptionName);
        sysLogMapper.insert(sysLog);
    }

    private String getMethodSysLogAnnotationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(SysLogs.class)) {
            //获取方法上注解中表明的权限
            SysLogs sysLogs = method.getAnnotation(SysLogs.class);
            return sysLogs.value();
        }
        return "未知";
    }
}
