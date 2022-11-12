package com.dawnfz.potcopyapi.utils;

import org.springframework.dao.DuplicateKeyException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/22 18:56
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [ExceptionDetailUtil类]
 *  异常工具类
 */
public class ExceptionUtil
{

    // 可能被自定义消息的异常类型列表
    private static final List<Class<?>> exClass = new ArrayList<>();

    static
    {
        exClass.add(DuplicateKeyException.class);
    }


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

    // 判断该异常是否在定义异常消息的异常范围内
    public static boolean isControlException(Exception ex)
    {
        return exClass.contains(ex.getClass());
    }
}