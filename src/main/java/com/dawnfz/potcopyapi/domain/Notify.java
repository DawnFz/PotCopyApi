package com.dawnfz.potcopyapi.domain;

import lombok.Data;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/18 13:54
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [Notify类]
 *  公告实体类
 */
@Data
public class Notify
{
    private Integer id;
    private String title;
    private String content;
    private String create;
    private String expire;
}
