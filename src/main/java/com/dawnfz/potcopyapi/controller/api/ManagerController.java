package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.InfoIncrement;
import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.service.abst.CopyInfoService;
import com.dawnfz.potcopyapi.service.abst.ManagerService;
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
 *  Date: 2022/11/14 19:37
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [ManagerController类]
 */
@RestController
@RequestMapping("/api/Manager")
@Tag(name = "Manager", description = "后台管理接口")
public class ManagerController
{

    private final ManagerService managerService;
    private final CopyInfoService copyInfoService;

    public ManagerController(ManagerService managerService, CopyInfoService copyInfoService)
    {
        this.managerService = managerService;
        this.copyInfoService = copyInfoService;
    }

    @RequestLimit(count = 2) //限制每分钟只能请求2次
    @Operation(summary = "用于增加新的标签(多)")
    @PostMapping("/addTags")
    public JsonResult addTags(@RequestParam("tagName") String[] tagNames) throws SQLException
    {
        boolean b = managerService.addTags(tagNames);
        return b ? ResultUtil.success("标签" + Arrays.toString(tagNames)
                + "添加成功") : ResultUtil.error("添加失败");
    }


    @RequestLimit(count = 2)
    @Operation(summary = "用于增加新的洞天区域(多)")
    @PostMapping("/addBlocks")
    public JsonResult addBlocks(@RequestParam("blockNames") String[] blockNames) throws SQLException
    {
        boolean b = managerService.addBlock(blockNames);
        return b ? ResultUtil.success("洞天区域" + Arrays.toString(blockNames)
                + "添加成功") : ResultUtil.error("添加失败");
    }

    @RequestLimit(count = 2)
    @Operation(summary = "用于关联洞天类型与洞天区域")
    @PostMapping("/addTypeBlocks")
    public JsonResult addTypeBlocks(@RequestParam("typeId") Integer typeId,
                                    @RequestParam("blockIds") Integer[] blockIds) throws SQLException
    {
        boolean b = managerService.addTypeBlock(typeId, blockIds);
        return b ? ResultUtil.success("类型[" + typeId + "]与区域" +
                Arrays.toString(blockIds) + "关联成功") : ResultUtil.error("添加失败");
    }

    @InfoIncrement
    @RequestLimit(count = 20)
    @Operation(summary = "根据 摹本编号 查询摹本信息[未审核]")
    @GetMapping("/copyInfo")
    public JsonResult getCopyInfo(@RequestParam("copyId")
                                  @Parameter(description = "摹本编号")
                                  String copyId) throws SQLException
    {
        CopyInfo copyInfo = copyInfoService.getCopyInfoById(copyId, 1);
        if (copyInfo == null) return ResultUtil.error("该摹本不存在");
        return ResultUtil.success(copyInfo);
    }

    @Operation(summary = "分页查询摹本信息[未审核]")
    @GetMapping("/managerInfos")
    public JsonResult managerInfos(@RequestParam("pageNum")
                                   @Parameter(description = "当前页数") Integer pageNum,
                                   @RequestParam("pageSize")
                                   @Parameter(description = "页内大小(数量)") Integer pageSize)
            throws SQLException
    {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        PageResult copyInfos = copyInfoService.getCopyInfos(pageRequest,
                null, null, null, null, null, 1);
        return ResultUtil.success(copyInfos);
    }

    @RequestLimit(count = 20)
    @Operation(summary = "根据摹本摹数删除一条摹本记录")
    @DeleteMapping("/delCopyInfo")
    public JsonResult delCopyInfo(@RequestParam("copyId")
                                  @Parameter(description = "摹本摹数")
                                  String copyId) throws SQLException
    {
        return managerService.delCopyInfo(copyId) ?
                ResultUtil.success("删除成功") : ResultUtil.error("删除失败");
    }


    @RequestLimit(count = 20)
    @Operation(summary = "修改一条摹本的审核状态[公开状态]")
    @PostMapping("/updateInfo")
    public JsonResult updateCopyInfo(@RequestParam("copyId")
                                     @Parameter(description = "摹本摹数") String copyId,
                                     @RequestParam("status")
                                     @Parameter(description = "审核状态") Integer status) throws SQLException
    {
        String statusTips = status == 1 ? "已取消公开状态" : "已改为公开状态";
        return managerService.updateCopyInfo(copyId, status) ?
                ResultUtil.success(statusTips) : ResultUtil.error("修改失败");
    }
}
