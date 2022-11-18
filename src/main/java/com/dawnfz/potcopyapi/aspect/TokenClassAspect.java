package com.dawnfz.potcopyapi.aspect;

import com.dawnfz.potcopyapi.annotation.AllowAnonymous;
import com.dawnfz.potcopyapi.annotation.Authorize;
import com.dawnfz.potcopyapi.helper.TokenHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/18 22:25
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TokenClassAspect类]
 */
@Aspect
@Component
public class TokenClassAspect
{
    TokenHelper tokenHelper;

    public TokenClassAspect(TokenHelper tokenHelper)
    {
        this.tokenHelper = tokenHelper;
    }

    // 配置切点
    @Around(value = "execution(* com.dawnfz.potcopyapi.controller.api.*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable
    {
        Object target = pjp.getTarget();
        Class<?> aClass = target.getClass();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();

        // 注解判断
        boolean hasAnnotation = aClass.isAnnotationPresent(Authorize.class);
        boolean hasAllowAnonymous = targetMethod.isAnnotationPresent(AllowAnonymous.class);

        if (hasAnnotation && !hasAllowAnonymous)
        {
            ServletRequestAttributes reqAtt = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            assert reqAtt != null;
            HttpServletRequest request = reqAtt.getRequest();
            HttpServletResponse response = reqAtt.getResponse();
            return tokenHelper.verityToken(pjp, request, response);
        }
        return pjp.proceed();
    }
}
