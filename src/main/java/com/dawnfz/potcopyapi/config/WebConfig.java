package com.dawnfz.potcopyapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/9 23:11
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [WebConfig类]
 *  WebMvc 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer
{
    // 重写跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
