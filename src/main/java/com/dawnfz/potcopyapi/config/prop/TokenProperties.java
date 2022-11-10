package com.dawnfz.potcopyapi.config.prop;

import com.dawnfz.potcopyapi.domain.AuthToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/10 12:26
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TokenProperties类]
 * JWT的token，区分大小写
 */

@Component
@ConfigurationProperties(prefix = "config.jwt")
public class TokenProperties
{

    private String scheme;
    private String secret;
    private long expire;
    private String header;

    // 生成Token
    public String createToken(AuthToken auth)
    {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);//过期时间

        Map<String, Object> claims = new HashMap<>();
        claims.put("auth", auth.getGrantee());
        return Jwts.builder()
                .setIssuer("PotCopy")
                .setHeaderParam("typ", "JWT")
                .addClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    // 获取Token中的注册信息
    public Claims getTokenClaim(String token)
    {
        try
        {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // 验证Token是否在有效期内
    public boolean isTokenExpired(Date expirationTime)
    {
        return expirationTime.before(new Date());
    }

    // 获取token失效时间
    public Date getExpirationDateFromToken(String token)
    {
        return getTokenClaim(token).getExpiration();
    }

    // 获取用户名从token中
    public String getUsernameFromToken(String token)
    {
        return getTokenClaim(token).getSubject();
    }

    // 获取jwt发布时间
    public Date getIssuedAtDateFromToken(String token)
    {
        return getTokenClaim(token).getIssuedAt();
    }


    public String getSecret()
    {
        return secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public long getExpire()
    {
        return expire;
    }

    public void setExpire(long expire)
    {
        this.expire = expire;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getScheme()
    {
        return scheme + " ";
    }

    public void setScheme(String scheme)
    {
        this.scheme = scheme;
    }
}