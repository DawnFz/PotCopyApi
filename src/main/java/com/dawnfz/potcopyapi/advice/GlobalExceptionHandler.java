package com.dawnfz.potcopyapi.advice;

import com.dawnfz.potcopyapi.wrapper.result.JsonResult;
import com.dawnfz.potcopyapi.wrapper.result.ResultUtil;
import com.dawnfz.potcopyapi.wrapper.result.Status;
import com.dawnfz.potcopyapi.utils.DateUtil;
import com.dawnfz.potcopyapi.utils.ExceptionUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/22 18:43
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [GlobalExceptionHandler类]
 *  Controller层 全局异常处理
 */
@ControllerAdvice
@ConfigurationProperties(prefix = "exception.log")
public class GlobalExceptionHandler
{
    private String exLogPath;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult errorHandler(HttpServletRequest req, Exception e) throws Exception
    {
        // 判断是否为自定义异常信息[操作异常]
        if (ExceptionUtil.isControlException(e))
        {
            return ResultUtil.error(e.getMessage());
        }
        System.err.println("+--------------------- GlobalExceptionHandler -------------------+\n");
        e.printStackTrace();
        System.err.println("\n+----------------------------------------------------------------+");
        String url = req.getRequestURL().toString();
        String fileName = DateUtil.getNowTime("yyyy_MM_dd_HHmmss") + ".log";
        String detail = ExceptionUtil.getExceptionDetail(e);
        try (OutputStream outputStream = new FileOutputStream(exLogPath + File.separator + fileName))
        {
            byte[] buff = detail.getBytes(StandardCharsets.UTF_8);
            for (byte by : buff)
            {
                outputStream.write(by);
            }
            outputStream.flush();
        }
        String template = "URL:" + url + " 中发生错误，日志已保存为 " + fileName + " 文件，请联系管理员处理";
        return ResultUtil.error(500, template, "服务器内部发生错误", Status.exception);
    }

    public String getExLogPath()
    {
        return exLogPath;
    }

    public void setExLogPath(String exLogPath)
    {
        this.exLogPath = exLogPath;
    }
}
