package com.dawnfz.potcopyapi.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/22 18:56
 *  Project: MiaoBlog
 *  Version: 1.0
 *  Describe: [ExceptionDetailUtil类]
 *  异常工具类
 */
public class ExceptionUtil
{

    // 从异常链的栈中获取异常详情信息
    public static String getExceptionDetail(Exception ex)
    {
        String ret = null;
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            ex.printStackTrace(printStream);
            ret = outputStream.toString();
            printStream.close();
            outputStream.close();
        }
        catch (Exception ignored) {}
        return ret;
    }

    public static String getExceptionDetail(Throwable ex)
    {
        StringWriter stringWriter = new StringWriter();
        try
        {
            PrintWriter printWriter = new PrintWriter(stringWriter, true);
            ex.printStackTrace(printWriter);
            printWriter.flush();
            stringWriter.flush();
        }
        catch (Exception ignored) {}
        return stringWriter.toString();
    }
}