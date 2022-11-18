package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.InfoIncrement;
import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.service.abst.CopyInfoService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
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
                typeId, blockId, server, tagNames, 0, null, null);
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
    public JsonResult addCopyInfo(@RequestBody Map<String, Object> params) throws SQLException
    {


        CopyInfo copyInfo = new CopyInfo();
        copyInfo.setCopyId((String) params.get("copyId"));
        copyInfo.setUid(0);
        copyInfo.setCopyName((String) params.get("copyName"));
        copyInfo.setTypeId((Integer) params.get("typeId"));
        copyInfo.setUploadType(Integer.parseInt((String) params.get("uploadType")));
        copyInfo.setBlockId((Integer) params.get("blockId"));
        copyInfo.setAuthor((String) params.get("author"));
        copyInfo.setServer(Integer.parseInt((String) params.get("server")));
        copyInfo.setOrigin((String) params.get("origin"));
        copyInfo.setDescription((String) params.get("description"));

        List<String> imageUrls = (List<String>) params.get("imageUrls");
        List<Integer> tagIds = (List<Integer>) params.get("tagIds");
        boolean b = copyInfoService.addCopyInfo(copyInfo, tagIds.toArray(new Integer[0]), imageUrls.toArray(new String[0]));
        if (!b) return ResultUtil.error("摹本发布失败");
        return ResultUtil.success("发布成功，审核中 [预计两小时]");
    }
}
