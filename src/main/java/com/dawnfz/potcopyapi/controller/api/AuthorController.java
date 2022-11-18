package com.dawnfz.potcopyapi.controller.api;

import com.dawnfz.potcopyapi.annotation.RequestLimit;
import com.dawnfz.potcopyapi.config.prop.TokenProperties;
import com.dawnfz.potcopyapi.domain.Author;
import com.dawnfz.potcopyapi.domain.CopyInfo;
import com.dawnfz.potcopyapi.domain.dto.AuthorDto;
import com.dawnfz.potcopyapi.service.abst.AuthorService;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/10 13:01
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [AuthorizedController类]
 */
@RestController
@RequestMapping("/api/Author")
@Tag(name = "Author", description = "创作者接口")
public class AuthorController
{
    private final AuthorService authorService;
    private final TokenProperties tokenProperties;

    private final CopyInfoService copyInfoService;

    public AuthorController(AuthorService authorService, TokenProperties tokenProperties,
                            CopyInfoService copyInfoService)
    {
        this.authorService = authorService;
        this.tokenProperties = tokenProperties;
        this.copyInfoService = copyInfoService;
    }

    @Operation(summary = "创作者或管理员的登录接口")
    @PostMapping(value = "/login")
    public JsonResult login(@RequestParam("username") @Parameter(description = "用户名") String username,
                            @RequestParam("password") @Parameter(description = "密码") String password) throws SQLException
    {
        Author author = authorService.login(username, password);
        if (author == null) return ResultUtil.error("登录失败，用户名或密码错误");
        AuthorDto authorized = tokenProperties.createToken(author);
        return ResultUtil.success("登录成功，欢迎您 [" + author.getNickName() + "]", authorized);
    }

    @RequestLimit(message = "每分钟只能分享十个摹本喔！") // 限制每分钟只能请求一次
    @Operation(summary = "合作的原创作者分享一个摹本")
    @PostMapping("/shareCopyInfoVerity")
    public JsonResult shareCopyInfoVerity(@RequestBody Map<String, Object> params,
                                          HttpServletRequest request) throws SQLException
    {
        Claims claims = (Claims) request.getAttribute("identityId");
        CopyInfo copyInfo = new CopyInfo();
        copyInfo.setCopyId((String) params.get("copyId"));
        copyInfo.setUid((Integer) claims.get("uid"));
        copyInfo.setCopyName((String) params.get("copyName"));
        copyInfo.setTypeId((Integer) params.get("typeId"));
        copyInfo.setUploadType(Integer.parseInt((String) params.get("uploadType")));
        copyInfo.setBlockId((Integer) params.get("blockId"));
        String nickName = (String) claims.get("nickName");
        copyInfo.setAuthor(nickName);
        copyInfo.setServer(Integer.parseInt((String) params.get("server")));
        copyInfo.setOrigin((String) params.get("origin"));
        copyInfo.setDescription((String) params.get("description"));

        List<String> imageUrls = (List<String>) params.get("imageUrls");
        List<Integer> tagIds = (List<Integer>) params.get("tagIds");
        boolean b = authorService.addCopyInfo(copyInfo, tagIds.toArray(new Integer[0]), imageUrls.toArray(new String[0]));
        if (!b) return ResultUtil.error("摹本发布失败");
        return ResultUtil.success("发布成功，等待CDN刷新后即可显示");
    }

    @Operation(summary = "分页查询摹本信息[我发布的]")
    @GetMapping("/mySharedInfos")
    public JsonResult mySharedInfos(@RequestParam("pageNum")
                                    @Parameter(description = "当前页数") Integer pageNum,
                                    @RequestParam("pageSize")
                                    @Parameter(description = "页内大小(数量)") Integer pageSize,
                                    HttpServletRequest request)
            throws SQLException
    {
        Claims claims = (Claims) request.getAttribute("identityId");
        Integer uid = (Integer) claims.get("uid");
        Integer roleLevel = (Integer) claims.get("roleLevel");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        PageResult copyInfos = copyInfoService.getCopyInfos(pageRequest, null,
                null, null, null, null, null, uid, roleLevel);
        return ResultUtil.success(copyInfos);
    }


    @PostMapping("/editCopyInfo")
    @Operation(summary = "修改我发布的摹本信息")
    public JsonResult editCopyInfo(@RequestBody Map<String, Object> params,
                                   HttpServletRequest request) throws SQLException
    {
        Claims claims = (Claims) request.getAttribute("identityId");
        CopyInfo copyInfo = new CopyInfo();
        copyInfo.setCopyId((String) params.get("copyId"));
        copyInfo.setCopyName((String) params.get("copyName"));
        copyInfo.setTypeId((Integer) params.get("typeId"));
        copyInfo.setBlockId((Integer) params.get("blockId"));
        String nickName = (String) claims.get("nickName");
        copyInfo.setAuthor(nickName);
        copyInfo.setServer((Integer) params.get("server"));
        copyInfo.setOrigin((String) params.get("origin"));
        copyInfo.setDescription((String) params.get("description"));
        List<String> images = (List<String>) params.get("images");
        List<Integer> tagIds = (List<Integer>) params.get("tagIds");
        authorService.updateCopyInfo(copyInfo, tagIds.toArray(new Integer[0]), images.toArray(new String[0]));
        return ResultUtil.success("修改成功，内容需要等待CDN刷新后才显示");
    }

}
