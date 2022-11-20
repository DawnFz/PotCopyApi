package com.dawnfz.potcopyapi.service.impl;

import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.domain.Report;
import com.dawnfz.potcopyapi.mapper.CopyInfoMapper;
import com.dawnfz.potcopyapi.mapper.ManagerMapper;
import com.dawnfz.potcopyapi.mapper.ParamsMapper;
import com.dawnfz.potcopyapi.mapper.ReportMapper;
import com.dawnfz.potcopyapi.service.abst.CopyInfoService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 18:59
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [CopyInfoServiceImpl类]
 */
@Service
@Transactional
public class CopyInfoServiceImpl implements CopyInfoService
{
    private final CopyInfoMapper copyInfoMapper;
    private final ParamsMapper paramsMapper;
    private final ManagerMapper managerMapper;
    private final ReportMapper reportMapper;

    public CopyInfoServiceImpl(CopyInfoMapper copyInfoMapper, ParamsMapper paramsMapper,
                               ManagerMapper managerMapper, ReportMapper reportMapper)
    {
        this.copyInfoMapper = copyInfoMapper;
        this.paramsMapper = paramsMapper;
        this.managerMapper = managerMapper;
        this.reportMapper = reportMapper;
    }

    // 添加(分享)一个摹本
    @Override
    public boolean addCopyInfo(CopyInfo copyInfo, Integer[] tagIds, String[] imageUrls) throws SQLException
    {
        try
        {
            String copyId = copyInfo.getCopyId();
            int addCnt = copyInfoMapper.addCopyInfo(copyInfo);
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
    public boolean delCopyInfo(String copyId, Integer uid) throws SQLException
    {
        int info = copyInfoMapper.delCopyInfo(copyId, uid);
        int tags = managerMapper.delTagForInfo(copyId);
        int imgs = managerMapper.delImageForInfo(copyId);
        return info > 0 && tags > 0 && imgs > 0;
    }

    @Override
    public boolean incCopyInfoHits(String copyId) throws SQLException
    {
        return copyInfoMapper.incCopyInfoHits(copyId) > 0;
    }

    @Override
    public boolean createCopyReport(Report report) throws SQLException
    {
        Report sourceReport = reportMapper.selectReport(report.getCopyId());
        if (sourceReport != null) return reportMapper.updateReport(report) > 0;
        return reportMapper.createReport(report) > 0;
    }

    @Override
    public boolean delReport(String copyId) throws SQLException
    {
        return false;
    }

    @Override
    public PageResult getAllReports(PageRequest pageRequest) throws SQLException
    {
        int pageSize = pageRequest.getPageSize();
        int pageNum = pageRequest.getPageNum();
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getAllReport();
        if (reports == null) reports = new ArrayList<>();
        return new PageResult(reports, page);
    }

    // 根据 洞天摹本的编号 获得摹本信息
    @Override
    public CopyInfo getCopyInfoById(String copyId, Integer status) throws SQLException
    {
        // status = 0 代表已过审核的摹本
        return copyInfoMapper.getCopyInfoById(copyId, status);
    }

    // 查询所有的洞天摹本[分页][模糊查询]
    @Override
    public PageResult getCopyInfos(PageRequest pageRequest, String copyName, Integer typeId, Integer blockId, Integer server,
                                   String[] tagNames, Integer status, Integer uid, Integer roleLevel) throws SQLException
    {
        int pageSize = pageRequest.getPageSize();
        int pageNum = pageRequest.getPageNum();
        String copyIdsStr = null;
        if (tagNames != null)
        {
            String tagNamesStr = generateSql(List.of(tagNames));
            List<String> copyIds = paramsMapper.getCopyIds(tagNamesStr);
            copyIdsStr = generateSql(copyIds);
        }
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        // status = 0 代表已过审核的摹本
        List<Object> copyInfos;
        if (status == null || status != 0)
            copyInfos = copyInfoMapper.getManagerCopyInfos(copyName, typeId, blockId, copyIdsStr, status, uid, roleLevel);
        else copyInfos = copyInfoMapper.getCopyInfos(copyName, typeId, blockId, server, copyIdsStr, status);
        if (copyInfos == null) copyInfos = new ArrayList<>();
        return new PageResult(copyInfos, page);
    }

    // 内部生成 Mybatis Mapper 中所需的 ${} 参数
    private String generateSql(List<String> list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : list)
        {
            stringBuilder.append("'")
                    .append(item)
                    .append("'")
                    .append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
