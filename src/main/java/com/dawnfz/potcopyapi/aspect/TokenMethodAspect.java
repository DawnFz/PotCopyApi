package com.dawnfz.potcopyapi.aspect;

import com.dawnfz.potcopyapi.annotation.Authorize;
import com.dawnfz.potcopyapi.helper.TokenHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/18 22:25
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TokenAspect类]
 */


@Aspect
@Component
public class TokenMethodAspect
{
    TokenHelper tokenHelper;

    public TokenMethodAspect(TokenHelper tokenHelper)
    {
        this.tokenHelper = tokenHelper;
    }

    // 配置切点
    @Pointcut("@annotation(authorize)")
    public void controllerAspect(Authorize authorize) {}

    @Around(value = "controllerAspect(authorize)", argNames = "pjp,authorize")
    public Object doAround(ProceedingJoinPoint pjp, Authorize authorize) throws Throwable
    {
        ServletRequestAttributes reqAtt = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        assert reqAtt != null;
        HttpServletRequest request = reqAtt.getRequest();
        HttpServletResponse response = reqAtt.getResponse();
        return tokenHelper.verityToken(pjp,request,response);
    }

}
