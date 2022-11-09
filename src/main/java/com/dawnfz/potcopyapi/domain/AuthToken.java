package com.dawnfz.potcopyapi.domain;

import lombok.Data;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 17:20
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthToken类]
 *  备用 - Api使用权 被授权者
 */
@Data
public class AuthToken
{
    private String grantee;

}
