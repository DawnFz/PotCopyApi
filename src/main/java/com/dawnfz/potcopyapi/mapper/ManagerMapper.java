package com.dawnfz.potcopyapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/14 19:41
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [ManagerMapper接口]
 */
@Mapper
public interface ManagerMapper
{
    // 添加一个标签到数据库
    int addTag(@Param("tagName") String tagName) throws SQLException;

    // 添加一个新的洞天类型
    int addBlock(@Param("blockName") String blockName) throws SQLException;

    // 添加一个与 洞天类型关联的 洞天区域
    int addTypeBlock(@Param("typeId") Integer typeId, @Param("blockId") Integer blockId) throws SQLException;

    // 删除一条洞天摹本记录
    int delCopyInfo(@Param("copyId") String copyId) throws SQLException;

    int delTagForInfo(@Param("copyId") String copyId) throws SQLException;

    int delImageForInfo(@Param("copyId") String copyId) throws SQLException;

    // 更新一条洞天摹本记录[修改审核状态]
    int updateCopyInfo(@Param("copyId") String copyId, @Param("status") Integer status) throws SQLException;
}
