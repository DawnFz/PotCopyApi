package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.service.abst.TagService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Arrays;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 21:18
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagController类]
 */
@Controller
@RequestMapping("/api/Tag")
@Tag(name = "Tag", description = "标签接口")
public class TagController
{
    private final TagService tagService;

    public TagController(TagService tagService)
    {
        this.tagService = tagService;
    }

    @ResponseBody
    @RequestLimit
    @Operation(summary = "[锁定页大小10]分页查询标签")
    @GetMapping("/tags")
    public JsonResult getTags(@RequestParam("pageNum")
                              @Parameter(description = "当前页数")
                              Integer pageNum) throws SQLException
    {
        // 分页限制每页只查询10条标签
        PageRequest pageRequest = new PageRequest(pageNum, 10);
        PageResult copyInfos = tagService.getTags(pageRequest);
        return ResultUtil.success(copyInfos);
    }

    @ResponseBody
    @RequestLimit(count = 2) //限制每分钟只能请求2次
    @Operation(summary = "[测试]用于增加新的标签(多)")
    @PostMapping("/addTags")
    public JsonResult addTags(@RequestParam("tagName") String[] tagNames) throws SQLException
    {
        boolean b = tagService.addTags(tagNames);
        return b ? ResultUtil.success("标签" + Arrays.toString(tagNames) + "添加成功") : ResultUtil.error("添加失败");
    }
}
