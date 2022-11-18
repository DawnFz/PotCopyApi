package com.dawnfz.potcopyapi.annotation;

import java.lang.annotation.*;

/*
 *  Type: Annotation
 *  Author: DawnFz.com
 *  Date: 2022/11/18 22:28
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [Authorized注解]
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE}) // 方法或类
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize
{}
