package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.domain.Block;
import com.dawnfz.potcopyapi.domain.PotType;
import com.dawnfz.potcopyapi.service.abst.ParamsService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 21:18
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagController类]
 */
@RestController
@RequestMapping("/api/Params")
@Tag(name = "Params", description = "额外参数接口")
public class ParamsController
{
    private final ParamsService paramsService;

    public ParamsController(ParamsService paramsService)
    {
        this.paramsService = paramsService;
    }

    @RequestLimit
    @Operation(summary = "[锁定页大小10]分页查询标签")
    @GetMapping("/tags")
    public JsonResult getTags(@RequestParam("pageNum")
                              @Parameter(description = "当前页数")
                              Integer pageNum) throws SQLException
    {
        // 分页限制每页只查询10条标签
        PageRequest pageRequest = new PageRequest(pageNum, 10);
        PageResult copyInfos = paramsService.getTags(pageRequest);
        return ResultUtil.success(copyInfos);
    }

    @RequestLimit(count = 15)
    @Operation(summary = "查询洞天类型")
    @GetMapping("/types")
    public JsonResult getPotTypes() throws SQLException
    {
        List<PotType> potTypes = paramsService.getPotTypes();
        return ResultUtil.success(potTypes);
    }

    @RequestLimit(count = 15)
    @Operation(summary = "查询对应洞天类型的区域")
    @GetMapping("/blocks")
    public JsonResult getBlocks(@RequestParam("typeId")
                                @Parameter(description = "洞天类型Id")
                                Integer typeId) throws SQLException
    {
        List<Block> blocks = paramsService.getBlocks(typeId);
        return ResultUtil.success(blocks);
    }
}
