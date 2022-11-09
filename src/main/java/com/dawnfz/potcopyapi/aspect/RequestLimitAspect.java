package com.dawnfz.potcopyapi.aspect;

import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/9 23:44
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [RequestLimitAspect类]
 *  Aop 切面 请求访问频率限制
 */

@Aspect
@Component
public class RequestLimitAspect
{
    private static final ConcurrentHashMap<String, ExpiringMap<String, Integer>> map = new ConcurrentHashMap<>();

    // 配置切点
    @Pointcut("@annotation(requestLimit)")
    public void controllerAspect(RequestLimit requestLimit) {}

    @Around(value = "controllerAspect(requestLimit)", argNames = "pjp,requestLimit")
    public Object doAround(ProceedingJoinPoint pjp, RequestLimit requestLimit) throws Throwable
    {
        // 获得request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        ExpiringMap<String, Integer> expiringMap = map.getOrDefault(request.getRequestURI(),
                ExpiringMap.builder().variableExpiration().build());
        Integer count = expiringMap.getOrDefault(request.getRemoteAddr(), 0);

        ExpirationPolicy created = ExpirationPolicy.CREATED;
        String rAddr = request.getRemoteAddr();
        long time = requestLimit.time();

        if (count >= requestLimit.count()) return ResultUtil.error("请求频率过快，请稍后再试");
        else if (count == 0) expiringMap.put(rAddr, count + 1, created, time, TimeUnit.MILLISECONDS);
        else expiringMap.put(request.getRemoteAddr(), count + 1);
        map.put(request.getRequestURI(), expiringMap);
        return pjp.proceed();
    }
}
