package com.dawnfz.potcopyapi.domain.dto;

import com.dawnfz.potcopyapi.domain.Author;
import lombok.Getter;
import lombok.Setter;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/17 23:53
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthorDto类]
 */
@Getter
@Setter
public class AuthorDto
{
    private Integer uid;
    private Integer roleLevel;
    private String nickName;
    private String permissions;
    private String token;

    public AuthorDto() {}

    public AuthorDto(Author author)
    {
        this.uid = author.getUid();
        this.nickName = author.getNickName();
        this.roleLevel = author.getRoleLevel();
    }

}
