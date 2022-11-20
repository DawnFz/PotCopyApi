package com.dawnfz.potcopyapi.mapper;

import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.domain.dto.CopyInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 18:22
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [CopyInfoMapper类]
 */
@Mapper
public interface CopyInfoMapper
{
    // 添加一条 摹本信息 到数据库
    int addCopyInfo(CopyInfo copyInfo) throws SQLException;

    int addCopyInfoVerity(CopyInfo copyInfo) throws SQLException;

    int updateCopyInfo(CopyInfo copyInfo) throws SQLException;

    int delCopyInfo(@Param("copyId") String copyId, @Param("uid") Integer uid) throws SQLException;

    // 添加一条 摹本图片信息 到数据库
    int addImageForCopyInfo(@Param("imageUrl") String imageUrl, @Param("copyId") String copyId);

    int delImageForCopyInfo(@Param("copyId") String copyId) throws SQLException;

    // 点击量自增
    int incCopyInfoHits(@Param("copyId") String copyId) throws SQLException;

    // 根据摹本 copyId 查询 摹本信息
    CopyInfo getCopyInfoById(@Param("copyId") String copyId,
                             @Param("status") Integer status) throws SQLException;

    // 查询所有的摹本[根据名称模糊]
    List<Object> getCopyInfos(@Param("copyName") String copyName,
                              @Param("typeId") Integer typeId,
                              @Param("blockId") Integer blockId,
                              @Param("server") Integer server,
                              @Param("copyIds") String copyIds,
                              @Param("status") Integer status) throws SQLException;

    List<Object> getManagerCopyInfos(@Param("copyName") String copyName,
                                     @Param("typeId") Integer typeId,
                                     @Param("blockId") Integer blockId,
                                     @Param("copyIds") String copyIds,
                                     @Param("status") Integer status,
                                     @Param("uid") Integer uid,
                                     @Param("roleLevel") Integer roleLevel) throws SQLException;

    // 查询所有的摹本[根据标签]
    List<CopyInfo> getCopyInfosByTags(@Param("tags") String[] tags) throws SQLException;
}
