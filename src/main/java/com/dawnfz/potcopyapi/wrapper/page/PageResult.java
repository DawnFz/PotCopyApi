package com.dawnfz.potcopyapi.wrapper.page;

import com.github.pagehelper.Page;

import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/23 23:46
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [PageResult类]
 *  分页查询结果包装
 */
public class PageResult
{
    // 当前页数
    private int pageNum;

    // 总查询记录
    private long totalSize;

    // 页码总数
    private int totalPages;

    // 结果列表
    private final List<?> content;

    public int getPageNum()
    {
        return pageNum;
    }

    public PageResult(List<?> content)
    {
        this.content = content;
    }

    public PageResult(List<?> content, Page<Object> page)
    {
        this.pageNum=page.getPageNum();
        this.totalSize = page.getTotal();
        this.totalPages = page.getPages();
        this.content = content;
    }

    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }

    public long getTotalSize()
    {
        return totalSize;
    }

    public void setTotalSize(long totalSize)
    {
        this.totalSize = totalSize;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }

    public List<?> getContent()
    {
        return content;
    }
}