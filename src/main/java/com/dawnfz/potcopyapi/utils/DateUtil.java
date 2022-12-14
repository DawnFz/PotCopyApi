package com.dawnfz.potcopyapi.utils;

import java.text.SimpleDateFormat;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/10/22 19:16
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [DateUtilš▒╗]
 */
public class DateUtil
{
    private static SimpleDateFormat sdf;

    public static String getNowTime(String pattern)
    {
        sdf = new SimpleDateFormat(pattern);
        return sdf.format(System.currentTimeMillis());
    }

    public static String getNowTime()
    {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }
}
