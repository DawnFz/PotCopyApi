package com.dawnfz.potcopyapi.service.abst;

import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.domain.Report;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;

import java.sql.SQLException;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/8 18:58
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [CopyInfoService接口]
 */
public interface CopyInfoService
{
    boolean addCopyInfo(CopyInfo copyInfo, Integer[] tagIds, String[] imageUrls) throws SQLException;

    boolean delCopyInfo(String copyId, Integer uid) throws SQLException;

    boolean incCopyInfoHits(String copyId) throws SQLException;

    boolean createCopyReport(Report report) throws SQLException;
    boolean delReport(String copyId) throws SQLException;

    PageResult getAllReports(PageRequest pageRequest) throws SQLException;

    CopyInfo getCopyInfoById(String copyId, Integer status) throws SQLException;

    PageResult getCopyInfos(PageRequest pageRequest, String copyName, Integer typeId, Integer blockId, Integer server,
                            String[] tagNames, Integer status, Integer uid, Integer roleLevel) throws SQLException;
}
