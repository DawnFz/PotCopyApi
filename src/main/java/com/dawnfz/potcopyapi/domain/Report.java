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
    private Integer reporter;
    private String copyId;
    private String note;
}
