package com.dawnfz.potcopyapi.interceptor;

import com.dawnfz.potcopyapi.config.prop.TokenProperties;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
    private final TokenProperties tokenProperties;

    public TokenInterceptor(TokenProperties tokenProperties)
    {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException
    {
        /* 地址过滤 */
        String uri = request.getRequestURI();
        if (uri.contains("/Auth")) return true;
        /* Token 验证 */
        String scheme = tokenProperties.getScheme();
        String sourceAuth = request.getHeader(tokenProperties.getHeader());
        String token = sourceAuth != null ? sourceAuth.replaceAll(scheme, "") : null;
        if (token == null) return setUnAuthorization(response);
        Claims claims = tokenProperties.getTokenClaim(token);
        if (claims == null) return setUnAuthorization(response);
        if (tokenProperties.isTokenExpired(claims.getExpiration())) return setUnAuthorization(response);
        /* 设置 identityId 用户身份ID */
        request.setAttribute("identityId", claims);
        return true;
    }

    private boolean setUnAuthorization(HttpServletResponse response)
    {
        response.setStatus(401);
        return false;
    }
}
