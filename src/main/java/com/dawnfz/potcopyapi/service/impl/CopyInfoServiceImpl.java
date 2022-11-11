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
        boolean tagsInsert = true;
        boolean imgsInsert = true;

        String copyId = copyInfo.getCopyId();
        CopyInfo info = copyInfoMapper.getCopyInfoById(copyId);
        if (info != null) return false;
        int addCnt = copyInfoMapper.addCopyInfo(copyInfo);
        if (addCnt == 0) return false;
        // 待优化
        for (Integer tagId : tagIds)
        {
            int tagCnt = paramsMapper.addTagForCopyInfo(tagId, copyId);
            if (tagCnt == 0) tagsInsert = false;
        }
        for (String imageUrl : imageUrls)
        {
            int imgCnt = copyInfoMapper.addImageForCopyInfo(imageUrl, copyId);
            if (imgCnt == 0) imgsInsert = false;
        }
        return tagsInsert && imgsInsert;
    }

    // 根据 洞天摹本的编号 获得摹本信息
    @Override
    public CopyInfo getCopyInfoById(String copyId) throws SQLException
    {
        return copyInfoMapper.getCopyInfoById(copyId);
    }

    // 查询所有的洞天摹本[分页][模糊查询]
    @Override
    public PageResult getCopyInfos(PageRequest pageRequest, String copyName, Integer typeId,
                                   Integer blockId, String[] tagNames) throws SQLException
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
        List<CopyInfoDto> copyInfos = copyInfoMapper.getCopyInfos(copyName,typeId,blockId, copyIdsStr);
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
