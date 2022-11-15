package com.dawnfz.potcopyapi.service.impl;

import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.domain.dto.CopyInfoDto;
import com.dawnfz.potcopyapi.mapper.CopyInfoMapper;
import com.dawnfz.potcopyapi.mapper.ParamsMapper;
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

    public CopyInfoServiceImpl(CopyInfoMapper copyInfoMapper, ParamsMapper paramsMapper)
    {
        this.copyInfoMapper = copyInfoMapper;
        this.paramsMapper = paramsMapper;
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
    public boolean incCopyInfoHits(String copyId) throws SQLException
    {
        return copyInfoMapper.incCopyInfoHits(copyId) > 0;
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
    public PageResult getCopyInfos(PageRequest pageRequest, String copyName, Integer typeId,
                                   Integer blockId,Integer server, String[] tagNames, Integer status) throws SQLException
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
        if (status == 0) copyInfos = copyInfoMapper.getCopyInfos(copyName, typeId, blockId, server,copyIdsStr, status);
        else copyInfos = copyInfoMapper.getManagerCopyInfos(copyName, typeId, blockId, copyIdsStr, status);
        long total = page.getTotal();
        int totalPages = page.getPages();
        if (copyInfos.size() == 0) copyInfos = new ArrayList<>();
        PageResult pageResult = new PageResult(copyInfos);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalSize(total);
        pageResult.setTotalPages(totalPages);
        return pageResult;
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
