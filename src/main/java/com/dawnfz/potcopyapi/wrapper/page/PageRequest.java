package com.dawnfz.potcopyapi.wrapper.page;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/23 23:51
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [PageRequest类]
 *  分页查询 页码请求模型实体
 */
public class PageRequest
{

    // 当前请求页码
    private final int pageNum;

    // 每页总数
    private final int pageSize;

    public PageRequest(int pageNum, int pageSize)
    {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getPageNum()
    {
        return pageNum;
    }

    public int getPageSize()
    {
        return pageSize;
    }
}
