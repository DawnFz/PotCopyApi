package com.dawnfz.potcopyapi.config;

import com.dawnfz.potcopyapi.interceptor.TokenInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/21 14:42
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [InterceptorConfig类]
 */

@Configuration
@ConfigurationProperties(prefix = "config.url")
public class TokenInterceptorConfig implements WebMvcConfigurer
{
    List<String> excludePaths;

    public List<String> getExcludePaths() {return excludePaths;}

    public void setExcludePaths(List<String> excludePaths) {this.excludePaths = excludePaths;}

    @Resource
    private TokenInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                // 白名单
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/*.html")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/**/*.html")
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns(excludePaths);
    }
}
