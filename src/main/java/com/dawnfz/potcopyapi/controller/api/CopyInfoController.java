package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.InfoIncrement;
import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.service.abst.CopyInfoService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 18:56
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [CopyInfoController类]
 */
@RestController
@RequestMapping("/api/Info")
@Tag(name = "CopyInfo", description = "摹本接口")
public class CopyInfoController
{
    private final CopyInfoService copyInfoService;

    public CopyInfoController(CopyInfoService copyInfoService)
    {
        this.copyInfoService = copyInfoService;
    }

    @RequestLimit(count = 30)
    @Operation(summary = "分页查询摹本信息[支持 名称/标签 模糊查询]")
    @GetMapping("/copyInfos")
    public JsonResult getCopyInfos(@RequestParam("pageNum")
                                   @Parameter(description = "当前页数") Integer pageNum,
                                   @RequestParam("pageSize")
                                   @Parameter(description = "页内大小(数量)") Integer pageSize,
                                   @RequestParam(value = "copyName", required = false)
                                   @Parameter(description = "名称模糊搜索参数") String copyName,
                                   @RequestParam(value = "typeId", required = false)
                                   @Parameter(description = "限定搜索洞天类型") Integer typeId,
                                   @RequestParam(value = "blockId", required = false)
                                   @Parameter(description = "限定搜索区域类型") Integer blockId,
                                   @RequestParam(value = "server", required = false)
                                   @Parameter(description = "限定搜索服务器类型") Integer server,
                                   @RequestParam(value = "tagNames", required = false)
                                   @Parameter(description = "标签模糊搜索参数") String[] tagNames)
            throws SQLException
    {
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        PageResult copyInfos = copyInfoService.getCopyInfos(pageRequest, copyName,
                typeId, blockId, server, tagNames, 0);
        return ResultUtil.success(copyInfos);
    }

    @InfoIncrement
    @RequestLimit(count = 20)
    @Operation(summary = "根据 摹本编号 查询摹本信息[已审核]")
    @GetMapping("/copyInfo")
    public JsonResult getCopyInfo(@RequestParam("copyId")
                                  @Parameter(description = "摹本编号")
                                  String copyId) throws SQLException
    {
        CopyInfo copyInfo = copyInfoService.getCopyInfoById(copyId, 0);
        if (copyInfo == null) return ResultUtil.error("该摹本不存在");
        return ResultUtil.success(copyInfo);
    }

    @RequestLimit(message = "每分钟只能分享两个摹本喔！", count = 2) // 限制每分钟只能请求一次
    @Operation(summary = "由玩家上传(分享)一个洞天摹本")
    @PostMapping("/shareCopyInfo")
    public JsonResult addCopyInfo(@RequestParam("copyId")
                                  @Parameter(description = "摹本摹数") String copyId,
                                  @RequestParam("copyName")
                                  @Parameter(description = "摹本名称") String copyName,
                                  @RequestParam("typeId")
                                  @Parameter(description = "洞天类型[id]") Integer typeId,
                                  @RequestParam("blockId")
                                  @Parameter(description = "所在区域[id]") Integer blockId,
                                  @RequestParam("server")
                                  @Parameter(description = "所在区域[id]") Integer server,
                                  @RequestParam("author")
                                  @Parameter(description = "摹本作者") String author,
                                  @RequestParam(value = "origin", required = false)
                                  @Parameter(description = "摹本来源") String origin,
                                  @RequestParam("tagIds")
                                  @Parameter(description = "标签Id") Integer[] tagIds,
                                  @RequestParam("imageUrls")
                                  @Parameter(description = "图片链接") String[] imageUrls,
                                  @RequestParam("description")
                                  @Parameter(description = "摹本简介/描述") String description)
            throws SQLException
    {
        CopyInfo copyInfo = new CopyInfo();
        copyInfo.setCopyId(copyId);
        copyInfo.setCopyName(copyName);
        copyInfo.setTypeId(typeId);
        copyInfo.setBlockId(blockId);
        copyInfo.setAuthor(author);
        copyInfo.setServer(server);
        copyInfo.setOrigin(origin);
        copyInfo.setDescription(description);
        boolean b = copyInfoService.addCopyInfo(copyInfo, tagIds, imageUrls);
        if (!b) return ResultUtil.error("摹本发布失败");
        return ResultUtil.success("发布成功，请等待审核 [非半夜时段预计两小时]");
    }
}
