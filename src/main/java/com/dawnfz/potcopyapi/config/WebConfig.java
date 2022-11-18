package com.dawnfz.potcopyapi.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/15 20:45
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [CorsConfig类]
 */
@Configuration
@ServletComponentScan
public class WebConfig implements WebMvcConfigurer
{
    // 跨域拦截器
    @Bean
    public FilterRegistrationBean<Filter> corsFilter()
    {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedOriginPattern("*");
        config.setMaxAge(18000L);
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // 设置监听器的优先级
        bean.setOrder(0);
        return bean;
    }
}

