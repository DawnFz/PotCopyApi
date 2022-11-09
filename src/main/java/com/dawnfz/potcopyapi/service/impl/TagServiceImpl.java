package com.dawnfz.potcopyapi.service.impl;

import com.dawnfz.potcopyapi.domain.Tag;
import com.dawnfz.potcopyapi.mapper.TagMapper;
import com.dawnfz.potcopyapi.service.abst.TagService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 21:17
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagServiceImpl类]
 */
@Service
@Transactional
public class TagServiceImpl implements TagService
{
    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper)
    {
        this.tagMapper = tagMapper;
    }

    @Override
    public boolean addTag(String tagName) throws SQLException
    {
        return tagMapper.addTag(tagName) > 0;
    }

    @Override
    public boolean addTags(String[] tagNames) throws SQLException
    {
        boolean addSuccess = true;
        for (String tagName : tagNames)
        {
            int cnt = tagMapper.addTag(tagName);
            if (cnt == 0) addSuccess = false;
        }
        return addSuccess;
    }

    @Override
    public PageResult getTags(PageRequest pageRequest) throws SQLException
    {
        int pageSize = pageRequest.getPageSize();
        int pageNum = pageRequest.getPageNum();
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Tag> tags = tagMapper.getTags();
        long total = page.getTotal();
        int totalPages = page.getPages();
        if (tags.size() == 0) tags = new ArrayList<>();
        PageResult pageResult = new PageResult(tags);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalSize(total);
        pageResult.setTotalPages(totalPages);
        return pageResult;
    }
}
