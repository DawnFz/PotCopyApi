package com.dawnfz.potcopyapi.mapper;

import com.dawnfz.potcopyapi.domain.PotType;
import com.dawnfz.potcopyapi.domain.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/8 20:58
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagMapper接口]
 */
@Mapper
public interface ParamsMapper
{
    // 添加一个标签到数据库
    int addTag(@Param("tagName") String tagName) throws SQLException;

    // 添加一条与 CopyInfo 关联的 Tag 到中间表
    int addTagForCopyInfo(@Param("tagId") Integer tagId, @Param("copyId") String copyId) throws SQLException;

    // 根据标签名获得标签Id [备用]
    Integer getTagByName(@Param("tagName") String tagName) throws SQLException;

    // 获取所有标签
    List<Tag> getTags() throws SQLException;

    // 根据标签从中间表查询含有该标签的 CopyInfo
    List<String> getCopyIds(String tagNames) throws SQLException;

    // 获得所有洞天类型
    List<PotType> getPotTypes() throws SQLException;
}
