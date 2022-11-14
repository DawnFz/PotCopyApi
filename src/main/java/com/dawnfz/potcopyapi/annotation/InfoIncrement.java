package com.dawnfz.potcopyapi.annotation;

import java.lang.annotation.*;

/*
 *  Type: Annotation
 *  Author: DawnFz.com
 *  Date: 2022/11/14 19:12
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [InfoIncrement注解]
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InfoIncrement
{
    int count() default 1;
}
