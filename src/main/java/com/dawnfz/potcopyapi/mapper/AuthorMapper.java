package com.dawnfz.potcopyapi.mapper;

import com.dawnfz.potcopyapi.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/17 22:10
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthorMapper接口]
 */
@Mapper
public interface AuthorMapper
{
    // select
    Author authorLogin(@Param("username") String username, @Param("password") String password) throws SQLException;

    Author getAuthorByUid(@Param("uid") Integer uid) throws SQLException;

    List<Author> getAuthors(@Param("nickName") String nickname) throws SQLException;

    // insert
    int addAuthor(@Param("author") Author author) throws SQLException;

    int updateAuthor(@Param("author") Author author) throws SQLException;


}
