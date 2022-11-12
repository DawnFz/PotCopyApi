package com.dawnfz.potcopyapi.annotation;

import java.lang.annotation.*;

/*
 *  Type: Annotation
 *  Author: DawnFz.com
 *  Date: 2022/11/9 23:42
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [RequestLimit注解]
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit
{
    String message() default "请求频率过快，请稍后再试";

    long time() default 60000;

    int count() default 10;

}
