package com.dawnfz.potcopyapi.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 18:17
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [CopyInfo类]
 */
@Getter
@Setter
public class CopyInfo
{
    private Integer id;
    private String copyId;
    private String copyName;
    private Integer typeId;
    private Integer blockId;
    private String uploadTime;
    private Integer uploadUid;
    private Integer hits;
    private String description;
    private Integer status;
    // 映射 - 字符串
    private String potType;
    private String blockName;

    // 映射 - 集合
    private List<String> images = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    // 重写 toString 方法[Json]
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("{");
        if (id != null) sb.append("\"id\":").append(id).append(",");
        if (copyId != null) sb.append("\"copyId\":\"").append(copyId).append("\",");
        if (copyName != null) sb.append("\"copyName\":\"").append(copyName).append("\",");
        if (typeId != null) sb.append("\"typeId\":").append(typeId).append(",");
        if (blockId != null) sb.append("\"blockId\":").append(blockId).append(",");
        if (uploadTime != null) sb.append("\"uploadTime\":\"").append(uploadTime).append("\",");
        if (uploadUid != null) sb.append("\"uploadUid\":").append(uploadUid).append(",");
        if (hits != null) sb.append("\"hits\":").append(hits).append(",");
        if (description != null) sb.append("\"description\":\"").append(description).append("\",");
        if (status != null) sb.append("\"status\":").append(status).append(",");
        if (potType != null) sb.append("\"potType\":\"").append(potType).append("\",");
        if (blockName != null) sb.append("\"blockName\":\"").append(blockName).append("\",");
        if (sb.lastIndexOf(",") != -1) sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}
