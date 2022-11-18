package com.dawnfz.potcopyapi.service.abst;

import com.dawnfz.potcopyapi.domain.Author;
import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;

import java.sql.SQLException;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/17 22:28
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthorService接口]
 */
public interface AuthorService
{
    boolean addAuthor(Author author) throws SQLException;

    boolean updateAuthor(Author author) throws SQLException;

    Author login(String username, String password) throws SQLException;

    Author getAuthorByUid(Integer uid) throws SQLException;

    PageResult getAuthors(PageRequest pageRequest, String nickName) throws SQLException;

    boolean addCopyInfo(CopyInfo copyInfo, Integer[] tagIds, String[] imageUrls) throws SQLException;

    boolean updateCopyInfo(CopyInfo copyInfo, Integer[] tagIds, String[] imageUrls) throws SQLException;
}
