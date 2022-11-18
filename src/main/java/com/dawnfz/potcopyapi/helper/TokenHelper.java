package com.dawnfz.potcopyapi.helper;

import com.alibaba.fastjson2.JSONObject;
import com.dawnfz.potcopyapi.config.prop.TokenProperties;
import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/18 22:28
 *  Project: WMS-Repository
 *  Version: 1.0
 *  Describe: [TokenHelper类]
 */
@Component
public class TokenHelper
{
    TokenProperties tokenProperties;
    public TokenHelper(TokenProperties tokenProperties)
    {
        this.tokenProperties = tokenProperties;
    }

    public Object verityToken(ProceedingJoinPoint pjp, HttpServletRequest request,
                                     HttpServletResponse response) throws Throwable
    {
        String uri = request.getRequestURI();
        if (uri.contains("/login")) return pjp.proceed();
        String scheme = tokenProperties.getScheme();
        String au = tokenProperties.getHeader();
        String sourceAuth = request.getHeader(au);
        String token = sourceAuth != null ? sourceAuth.replaceAll(scheme, "") : null;
        if (token == null)
        {
            response.setStatus(401);
            response.getWriter().write("");
            return null;
        }
        Claims claims;
        claims = tokenProperties.getTokenClaim(token);
        if (claims == null)
        {
            response.setStatus(401);
            response.getWriter().write("");
            return null;
        }
        if (!tokenProperties.roleLevelVerity(uri, claims))
        {
            JsonResult jsonResult = ResultUtil.error(401,"你没有权限访问该页面");
            response.setContentType("text/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(jsonResult));
            return null;
        }
        /* 设置 identityId 用户身份ID */
        request.setAttribute("identityId", claims);
        return pjp.proceed();
    }
}
