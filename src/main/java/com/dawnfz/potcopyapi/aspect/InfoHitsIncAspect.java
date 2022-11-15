package com.dawnfz.potcopyapi.aspect;

import com.dawnfz.potcopyapi.annotation.InfoIncrement;
import com.dawnfz.potcopyapi.service.abst.CopyInfoService;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/14 19:11
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [InfoHitsIncAspect类]
 */
@Aspect
@Component
public class InfoHitsIncAspect
{

    private final CopyInfoService copyInfoService;

    public InfoHitsIncAspect(CopyInfoService copyInfoService)
    {
        this.copyInfoService = copyInfoService;
    }

    private static final ConcurrentHashMap<String, ExpiringMap<String, Integer>> map = new ConcurrentHashMap<>();

    // 配置切点
    @Pointcut("@annotation(infoIncrement)")
    public void controllerAspect(InfoIncrement infoIncrement) {}

    @Around(value = "controllerAspect(infoIncrement)", argNames = "pjp,infoIncrement")
    public Object doAround(ProceedingJoinPoint pjp, InfoIncrement infoIncrement) throws Throwable
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
        // 超出每日点击数则停止自增
        if (count >= infoIncrement.count()) return pjp.proceed();
        else if (count == 0) expiringMap.put(rAddr, count + 1, created, 1800000L, TimeUnit.MILLISECONDS);
        else expiringMap.put(request.getRemoteAddr(), count + 1);
        map.put(request.getRequestURI(), expiringMap);

        // 从request取得 copyId 进行 Hits 自增
        String copyId = request.getParameter("copyId");
        copyInfoService.incCopyInfoHits(copyId);
        return pjp.proceed();
    }
}
