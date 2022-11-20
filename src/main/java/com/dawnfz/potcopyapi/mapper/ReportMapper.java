package com.dawnfz.potcopyapi.mapper;

import com.dawnfz.potcopyapi.domain.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/20 20:29
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [ReportMapper类]
 */
@Mapper
public interface ReportMapper
{
    int createReport(Report report) throws SQLException;

    int updateReport(Report report) throws SQLException;

    int deleteReport(@Param("copyId") String copyId) throws SQLException;

    Report selectReport(@Param("copyId") String copyId) throws SQLException;

    List<Report> getAllReport() throws SQLException;
}
