package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.config.prop.TokenProperties;
import com.dawnfz.potcopyapi.domain.AuthToken;
import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.*;


/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/10 13:01
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthorizedController类]
 */
@RestController
@RequestMapping("/api/Auth")
@Tag(name = "Auth", description = "授权接口[测试期间启用]")
@ConfigurationProperties(prefix = "config.auth")
public class AuthController
{
    private final TokenProperties tokenProperties;

    public AuthController(TokenProperties tokenProperties)
    {
        this.tokenProperties = tokenProperties;
    }

    private String sourceKey;

    public String getSourceKey() {return sourceKey;}

    public void setSourceKey(String sourceKey) {this.sourceKey = sourceKey;}

    @Operation(summary = "根据授予的Key获得可使用授权部分接口的Token")
    @PostMapping(value = "/getToken")
    public JsonResult login(@RequestParam("grantee") @Parameter(description = "获取者") String grantee,
                            @RequestParam("key") @Parameter(description = "授权密钥") String key)
    {
        AuthToken authToken = new AuthToken();
        authToken.setGrantee(grantee);
        if (sourceKey.equals(key))
        {
            String token = tokenProperties.createToken(authToken);
            return ResultUtil.success("verify success", token);
        }
        return ResultUtil.error("Key验证错误");
    }
}
