package com.dawnfz.potcopyapi.service.abst;

import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.exception.ControlException;
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

    boolean incCopyInfoHits(String copyId) throws SQLException;

    CopyInfo getCopyInfoById(String copyId,Integer status) throws SQLException;

    PageResult getCopyInfos(PageRequest pageRequest, String copyName, Integer typeId, Integer blockId, String[] tagNames,Integer status) throws SQLException;
}
