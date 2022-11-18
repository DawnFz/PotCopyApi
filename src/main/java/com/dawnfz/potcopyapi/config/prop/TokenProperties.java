package com.dawnfz.potcopyapi.config.prop;

import com.dawnfz.potcopyapi.domain.Author;
import com.dawnfz.potcopyapi.domain.dto.AuthorDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    private Map<Integer, String> roleMap; // 0: Manager 1: Author
    private String scheme;
    private String secret;
    private long expire;
    private String header;

    // 生成Token
    public AuthorDto createToken(Author author)
    {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);//过期时间
        Integer roleLevel = author.getRoleLevel();
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", author.getUid());
        claims.put("roleLevel", roleLevel);
        claims.put("nickName", author.getNickName());
        String token = Jwts.builder()
                .setIssuer("PotCopy")
                .setHeaderParam("typ", "JWT")
                .addClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        String permission = roleMap.get(roleLevel);
        AuthorDto authorDto = new AuthorDto(author);
        authorDto.setPermissions(permission);
        authorDto.setToken(token);
        return authorDto;

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

    // 验证权限
    public boolean roleLevelVerity(String uri, Claims claims)
    {
        Integer roleLevel = (Integer) claims.get("roleLevel");
        if (roleLevel == 0) return true;
        String s = roleMap.get(roleLevel);
        return uri.contains(s);
    }

    //// Getter & Setter

    public String getSecret() {return secret;}

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

    public Map<Integer, String> getRoleMap() {return roleMap;}

    public void setRoleMap(Map<Integer, String> roleMap) {this.roleMap = roleMap;}
}