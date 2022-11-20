package com.dawnfz.potcopyapi.domain;

import lombok.Data;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/18 13:56
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [Report类]
 *  举报实体类
 */
@Data
public class Report
{
    private Integer id;
    private String author;
    private String copyId;
    private String origin;
    private String note;
    private Integer count;
    private String time;
}
