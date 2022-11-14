package com.dawnfz.potcopyapi.service.abst;

import com.dawnfz.potcopyapi.domain.Block;
import com.dawnfz.potcopyapi.domain.PotType;
import com.dawnfz.potcopyapi.exception.ControlException;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/*
 *  Type: Interface
 *  Author: DawnFz.com
 *  Date: 2022/11/8 21:17
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagService接口]
 */
public interface ParamsService
{
    PageResult getTags(PageRequest pageRequest) throws SQLException;

    List<PotType> getPotTypes() throws SQLException;

    List<Block> getBlocks(Integer typeId) throws SQLException;
}
