package com.dawnfz.potcopyapi.service.impl;

import com.dawnfz.potcopyapi.domain.Author;
import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.mapper.AuthorMapper;
import com.dawnfz.potcopyapi.mapper.CopyInfoMapper;
import com.dawnfz.potcopyapi.mapper.ParamsMapper;
import com.dawnfz.potcopyapi.service.abst.AuthorService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/17 22:28
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthorServiceImpl类]
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService
{
    private final AuthorMapper authorMapper;
    private final CopyInfoMapper copyInfoMapper;

    private final ParamsMapper paramsMapper;

    public AuthorServiceImpl(AuthorMapper authorMapper, CopyInfoMapper copyInfoMapper, ParamsMapper paramsMapper)
    {
        this.authorMapper = authorMapper;
        this.copyInfoMapper = copyInfoMapper;
        this.paramsMapper = paramsMapper;
    }

    @Override
    public boolean addAuthor(Author author) throws SQLException
    {
        try
        {
            return authorMapper.addAuthor(author) > 0;
        }
        catch (DuplicateKeyException e)
        {
            throw new DuplicateKeyException("该用户名已存在，请尝试使用其他用户名");
        }
    }

    @Override
    public boolean updateAuthor(Author author) throws SQLException
    {
        return authorMapper.updateAuthor(author) > 0;
    }

    @Override
    public Author login(String username, String password) throws SQLException
    {
        return authorMapper.authorLogin(username, password);
    }

    @Override
    public Author getAuthorByUid(Integer uid) throws SQLException
    {
        return authorMapper.getAuthorByUid(uid);
    }

    @Override
    public PageResult getAuthors(PageRequest pageRequest, String nickName) throws SQLException
    {
        int pageSize = pageRequest.getPageSize();
        int pageNum = pageRequest.getPageNum();
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Author> authors = authorMapper.getAuthors(nickName);
        return new PageResult(authors, page);
    }

    @Override
    public boolean addCopyInfo(CopyInfo copyInfo, Integer[] tagIds, String[] imageUrls) throws SQLException
    {
        try
        {
            String copyId = copyInfo.getCopyId();
            int addCnt = copyInfoMapper.addCopyInfoVerity(copyInfo);
            if (addCnt == 0) return false;
            for (Integer tagId : tagIds)
            {
                paramsMapper.addTagForCopyInfo(tagId, copyId);
            }
            for (String imageUrl : imageUrls)
            {
                copyInfoMapper.addImageForCopyInfo(imageUrl, copyId);
            }
            return true;
        }
        catch (DuplicateKeyException e)
        {
            throw new DuplicateKeyException("无法添加，因为已存在该摹本信息");
        }
    }

    @Override
    public boolean updateCopyInfo(CopyInfo copyInfo, Integer[] tagIds, String[] imageUrls) throws SQLException
    {
        try
        {
            String copyId = copyInfo.getCopyId();
            int addCnt = copyInfoMapper.updateCopyInfo(copyInfo);
            if (addCnt == 0) return false;
            paramsMapper.deleteTagForCopyInfo(copyId);
            for (Integer tagId : tagIds)
            {
                paramsMapper.addTagForCopyInfo(tagId, copyId);
            }
            copyInfoMapper.delImageForCopyInfo(copyId);
            for (String imageUrl : imageUrls)
            {
                copyInfoMapper.addImageForCopyInfo(imageUrl, copyId);
            }
            return true;
        }
        catch (DuplicateKeyException e)
        {
            throw new DuplicateKeyException("无法修改，原因：" + e.getMessage());
        }
    }
}
