package com.dawnfz.potcopyapi.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.dawnfz.potcopyapi.config.prop.TokenProperties;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/21 14:48
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TokenHandler类]
 */

@Component
public class TokenInterceptor implements HandlerInterceptor
{

    @Resource
    private TokenProperties tokenProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws SignatureException, IOException
    {
        /* 地址过滤 */
        String uri = request.getRequestURI();
        if (uri.contains("/Auth"))
        {
            return true;
        }
        /* Token 验证 */
        String scheme = tokenProperties.getScheme();
        String sourceAuth = request.getHeader(tokenProperties.getHeader());
        String token = sourceAuth != null ? sourceAuth.replaceAll(scheme, "") : null;
        if (token == null)
        {
            response.setStatus(401);
            response.getWriter().write("");
            return false;
        }

        Claims claims;
        try
        {
            claims = tokenProperties.getTokenClaim(token);
            if (claims == null)
            {
                response.setStatus(401);
                response.getWriter().write("");
                return false;
            }
            if (tokenProperties.isTokenExpired(claims.getExpiration()))
            {
                response.setStatus(401);
                response.getWriter().write("");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("+-------------- Exception -------------+");
            System.err.println(":" + e.getMessage());
            System.err.println("+--------------------------------------+");
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(ResultUtil.error(e.getMessage())));
            return false;
        }

        /* 设置 identityId 用户身份ID */
        request.setAttribute("identityId", claims);
        return true;
    }
}
