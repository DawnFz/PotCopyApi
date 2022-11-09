package com.dawnfz.potcopyapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        .version("v1"));
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

