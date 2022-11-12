package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.domain.Block;
import com.dawnfz.potcopyapi.domain.PotType;
import com.dawnfz.potcopyapi.exception.ControlException;
import com.dawnfz.potcopyapi.service.abst.ParamsService;
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
import java.util.*;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 21:18
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagController类]
 */
@Controller
@RequestMapping("/api/Params")
@Tag(name = "Params", description = "额外参数接口")
public class ParamsController
{
    private final ParamsService paramsService;

    public ParamsController(ParamsService paramsService)
    {
        this.paramsService = paramsService;
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
        PageResult copyInfos = paramsService.getTags(pageRequest);
        return ResultUtil.success(copyInfos);
    }

    @ResponseBody
    @RequestLimit(count = 15)
    @Operation(summary = "查询洞天类型")
    @GetMapping("/types")
    public JsonResult getPotTypes() throws SQLException
    {
        List<PotType> potTypes = paramsService.getPotTypes();
        return ResultUtil.success(potTypes);
    }


    @ResponseBody
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

    @ResponseBody
    @RequestLimit(count = 2) //限制每分钟只能请求2次
    @Operation(summary = "用于增加新的标签(多)")
    @PostMapping("/addTags")
    public JsonResult addTags(@RequestParam("tagName") String[] tagNames) throws SQLException, ControlException
    {
        boolean b = paramsService.addTags(tagNames);
        return b ? ResultUtil.success("标签" + Arrays.toString(tagNames) + "添加成功") : ResultUtil.error("添加失败");
    }


    @ResponseBody
    @RequestLimit(count = 2)
    @Operation(summary = "用于增加新的洞天区域(多)")
    @PostMapping("/addBlocks")
    public JsonResult addBlocks(@RequestParam("blockNames") String[] blockNames) throws SQLException, ControlException
    {
        boolean b = paramsService.addBlock(blockNames);
        return b ? ResultUtil.success("洞天区域" + Arrays.toString(blockNames) + "添加成功") : ResultUtil.error("添加失败");
    }

    @ResponseBody
    @RequestLimit(count = 2)
    @Operation(summary = "用于增加新的洞天区域(多)")
    @PostMapping("/addTypeBlocks")
    public JsonResult addTypeBlocks(@RequestParam("typeIds") Integer[] typeIds,
                                    @RequestParam("blockIds") Integer[] blockIds) throws SQLException
    {
        if (typeIds.length != blockIds.length) return ResultUtil.error("参数有误，数据数量不匹配");
        Map<Integer, Integer> blockMap = new HashMap<>();
        for (int i = 0; i < typeIds.length; i++)
        {
            blockMap.put(typeIds[i], blockIds[i]);
        }
        boolean b = paramsService.addTypeBlock(blockMap);
        List<Block> blocks = new ArrayList<>();
        blockMap.forEach((typeId, blockId) ->
        {
            Block block = new Block();
            block.setTypeId(typeId);
            block.setBlockId(blockId);
            blocks.add(block);
        });
        return b ? ResultUtil.success("类型与区域" + blocks + "关联成功") : ResultUtil.error("添加失败");
    }
}
