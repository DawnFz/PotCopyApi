package com.dawnfz.potcopyapi.wrapper.result;


/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/21 14:13
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [JsonResult类]
 */
public class ResultUtil
{

    public static JsonResult success()
    {
        return new JsonResult(null, Status.success);
    }

    public static JsonResult success(Object data)
    {
        return new JsonResult(data, Status.success);
    }

    public static JsonResult success(int code, Object data)
    {
        return new JsonResult(code, null, data, Status.success);
    }

    public static JsonResult success(String message, Object data)
    {
        return new JsonResult(200, message, data, Status.success);
    }

    public static JsonResult error()
    {
        return new JsonResult(null, Status.error);
    }

    public static JsonResult error(String message)
    {
        return new JsonResult(message, null, Status.error);
    }

    public static JsonResult error(int code, String message)
    {
        return new JsonResult(code, message, null, Status.error);
    }

    public static JsonResult error(int code, Object data, String message, Status status)
    {
        return new JsonResult(code, message, data, status);
    }


}
