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

    // 添加一条 摹本图片信息 到数据库
    int addImageForCopyInfo(@Param("imageUrl") String imageUrl, @Param("copyId") String copyId);

    // 根据摹本 copyId 查询 摹本信息
    CopyInfo getCopyInfoById(@Param("copyId") String copyId) throws SQLException;

    // 查询所有的摹本[根据名称模糊]
    List<CopyInfoDto> getCopyInfos(@Param("copyName") String copyName,
                                   @Param("typeId") Integer typeId,
                                   @Param("blockId") Integer blockId,
                                   @Param("copyIds") String copyIds) throws SQLException;

    // 查询所有的摹本[根据标签]
    List<CopyInfo> getCopyInfosByTags(@Param("tags") String[] tags) throws SQLException;
}
