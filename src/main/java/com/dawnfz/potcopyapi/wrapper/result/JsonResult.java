package com.dawnfz.potcopyapi.wrapper.result;

import com.alibaba.fastjson2.JSONObject;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/21 14:40
 *  Project: MiaoBlog
 *  Version: 1.0
 *  Describe: [JsonResult类]
 */
public class JsonResult
{
    protected int code;
    protected String message;
    protected Object data;
    protected Status status;

    //若状态为错误，则不返回数据体，只返回内置的错误消息及错误代码
    protected JsonResult(Object data, Status status)
    {

        this.status = status;
        if (status == Status.success)
        {
            this.code = 200;
            this.message = "success";
            this.data = data;
        }
        else if (status == Status.error)
        {
            this.message = "error";
            this.code = 500;
        }
    }


    //若状态为错误，则不返回数据体，只返回错误信息与内置的错误代码
    protected JsonResult(String message, Object data, Status status)
    {
        this.message = message;
        this.status = status;
        if (status == Status.success)
        {
            this.code = 200;
            this.data = data;
        }
        else if (status == Status.error)
        {
            this.code = 500;
        }
    }

    //若状态为错误，则不返回数据体，只返回错误信息及错误代码
    protected JsonResult(int code, String message, Object data, Status status)
    {
        this.code = code;
        if (status == Status.success || status == Status.exception)
        {
            this.message = message;
            this.data = data;
        }
        else if (status == Status.error)
        {
            this.message = message;
        }
        this.status = status;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public String toJson()
    {
        return JSONObject.toJSONString(this);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("{");
        if (message != null) sb.append("\"message\":\"").append(message).append("\",");
        else sb.append("\"message\":").append("null,");
        if (data != null) sb.append("\"data\":").append(data).append(",");
        else sb.append("\"data\":").append("null,");
        if (status != null) sb.append("\"status\":").append(status).append(",");
        else sb.append("\"status\":").append("null,");
        if (sb.lastIndexOf(",") != -1) sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append('}');
        return sb.toString();
    }
}