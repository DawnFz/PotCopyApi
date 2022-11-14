package com.dawnfz.potcopyapi.service.abst;

import java.sql.SQLException;
import java.util.Map;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/14 19:43
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [ManagerService接口]
 */
public interface ManagerService
{
    boolean addTag(String tagName) throws SQLException;

    boolean addTags(String[] tagNames) throws SQLException;

    boolean addBlock(String[] blockName) throws SQLException;

    boolean addTypeBlock(Integer typeId, Integer[] blockIds) throws SQLException;

    boolean delCopyInfo(String copyId) throws SQLException;

    boolean updateCopyInfo(String copyId, Integer status) throws SQLException;
}
