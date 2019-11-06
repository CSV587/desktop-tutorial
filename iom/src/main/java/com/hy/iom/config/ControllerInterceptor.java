package com.hy.iom.config;

import cn.haoyitec.common.RequestResult;
import com.hy.iom.base.config.BasicConfig;
import com.hy.iom.common.page.ChartResult;
import com.hy.iom.common.page.PageResult;
import com.hy.iom.common.page.PageResultTM;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * @author zxd
 * @since 2018/7/5.
 */
@Aspect
@Component
@Slf4j
public class ControllerInterceptor {
    private static final Integer EXCEPTION_CODE = -2;

    @Autowired
    public ControllerInterceptor() {
    }

    @Pointcut("execution(* com.hy.iom..*.*Controller.*(..))")
    public void execService() {
    }

    @Around("execService()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) ra;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Type t = method.getAnnotatedReturnType().getType();

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String url = BasicConfig.getIPMURL() + "/log/add";

        LocalTime startTime = LocalTime.now();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error("", e);
            LogRemoteWrite.write(url, request, response, methodName, e, 0, targetName);
            if (t.equals(PageResult.class)) {
                return PageResult.fail(e.getMessage(), EXCEPTION_CODE);
            }
            if (t.equals(ChartResult.class)) {
                return ChartResult.failure(e.getMessage());
            }
            if (t.equals(PageResultTM.class)) {
                return PageResultTM.fail(e.getMessage(), EXCEPTION_CODE);
            }
            return PageResult.fail(e.getMessage(), EXCEPTION_CODE);
        }
        long responseTime = ChronoUnit.MILLIS.between(startTime, LocalDateTime.now());

        LogRemoteWrite.write(url, request, response, methodName, null, responseTime, targetName);
        log.info("url : {}, method name : {}, target name : {}", url, methodName, targetName);
        return result;
    }
}