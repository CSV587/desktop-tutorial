package com.hy.config;

import com.hy.base.api.BasicConfig;
import com.hy.base.page.ResponseResult;
import com.hy.error.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
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

    /**
     * .
     * 切面方法
     *
     * @param joinPoint 程序切入对象
     * @return 返回值
     * @throws Throwable Throwable
     */
    @Around("execution(* com.hy.*.*.*.*Controller.*(..))")
    public Object doAround(final ProceedingJoinPoint joinPoint)
        throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes
            = (ServletRequestAttributes) ra;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String url = BasicConfig.getIpmUrl() + "/log/add";

        LocalTime startTime = LocalTime.now();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
            LogRemoteWrite.write(
                url,
                request,
                response,
                methodName,
                e,
                0,
                targetName);
            return ResponseResult.fail(e.getMessage());
        }
        long responseTime =
            ChronoUnit.MILLIS.between(
                startTime,
                LocalDateTime.now());

        LogRemoteWrite.write(url,
            request,
            response,
            methodName,
            null,
            responseTime,
            targetName);
        log.info("url : {}, method name : {}, target name : {}",
            url,
            methodName,
            targetName);
        return result;
    }
}
