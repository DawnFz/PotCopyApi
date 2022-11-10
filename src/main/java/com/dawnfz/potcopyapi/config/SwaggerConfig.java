package com.dawnfz.potcopyapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 22:01
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [SwaggerConfig类]
 *  Swagger 配置类
 */
@Configuration
public class SwaggerConfig
{

    //向容器添加 OpenApi Bean 对象
    @Bean
    public OpenAPI mallTinyOpenAPI()
    {
        return new OpenAPI()
                .info(new Info().title("PotCopy.WebApi")
                        .version("v1"))
                // 此部分仅作为限制添加记录接口被滥用
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("在下方输入管理员授权的 Token [无需前缀]")));
    }

    // OpenApi的分组
    @Bean
    public GroupedOpenApi publicApi()
    {
        return GroupedOpenApi.builder()
                .group("PotCopyApi.V1")
                .pathsToMatch("/**")
                .build();
    }
}

