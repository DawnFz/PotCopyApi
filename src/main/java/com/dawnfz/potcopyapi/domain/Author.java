package com.dawnfz.potcopyapi.domain;

import lombok.Getter;
import lombok.Setter;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 17:20
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [Author 用户类]
 */
@Getter
@Setter
public class Author
{
    private Integer uid;
    private Integer roleLevel;
    private String nickName;
    private String username;
    private String password;
    private String email;
    private String description;
}
