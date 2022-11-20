package com.dawnfz.potcopyapi.service.impl;

import com.dawnfz.potcopyapi.mapper.ManagerMapper;
import com.dawnfz.potcopyapi.mapper.ReportMapper;
import com.dawnfz.potcopyapi.service.abst.ManagerService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/14 19:43
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [ManagerServiceImpl类]
 */
@Service
@Transactional
public class ManagerServiceImpl implements ManagerService
{

    private final ManagerMapper managerMapper;
    private final ReportMapper reportMapper;

    public ManagerServiceImpl(ManagerMapper managerMapper, ReportMapper reportMapper)
    {
        this.managerMapper = managerMapper;
        this.reportMapper = reportMapper;
    }

    @Override
    public boolean addTag(String tagName) throws SQLException
    {
        return managerMapper.addTag(tagName) > 0;
    }

    @Override
    public boolean addTags(String[] tagNames) throws SQLException
    {
        try
        {
            boolean addSuccess = true;
            for (String tagName : tagNames)
            {
                int cnt = managerMapper.addTag(tagName);
                if (cnt == 0) addSuccess = false;
            }
            return addSuccess;
        }
        catch (DuplicateKeyException e)
        {
            throw new DuplicateKeyException("不允许添加重复标签");
        }
    }

    @Override
    public boolean addBlock(String[] blockNames) throws SQLException
    {
        try
        {
            boolean addSuccess = true;
            for (String blockName : blockNames)
            {
                int cnt = managerMapper.addBlock(blockName);
                if (cnt == 0) addSuccess = false;
            }
            return addSuccess;
        }
        catch (DuplicateKeyException e)
        {
            throw new DuplicateKeyException("不允许添加重复区域");
        }
    }

    @Override
    public boolean addTypeBlock(Integer typeId, Integer[] blockIds) throws SQLException
    {
        boolean addSuccess = true;
        for (Integer blockId : blockIds)
        {
            int cnt = managerMapper.addTypeBlock(typeId, blockId);
            if (cnt == 0) addSuccess = false;
        }
        return addSuccess;
    }

    @Override
    public boolean delCopyInfo(String copyId) throws SQLException
    {
        int info = managerMapper.delCopyInfo(copyId);
        int tags = managerMapper.delTagForInfo(copyId);
        int imgs = managerMapper.delImageForInfo(copyId);
        return info > 0 && tags > 0 && imgs > 0;
    }

    @Override
    public boolean delReport(String copyId) throws SQLException
    {
        return reportMapper.deleteReport(copyId) > 0;
    }

    @Override
    public boolean updateCopyInfo(String copyId, Integer status) throws SQLException
    {
        return managerMapper.updateCopyInfo(copyId, status) > 0;
    }

}
