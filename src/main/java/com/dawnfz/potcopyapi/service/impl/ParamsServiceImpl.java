package com.dawnfz.potcopyapi.service.impl;

import com.dawnfz.potcopyapi.domain.Block;
import com.dawnfz.potcopyapi.domain.PotType;
import com.dawnfz.potcopyapi.domain.Tag;
import com.dawnfz.potcopyapi.mapper.ParamsMapper;
import com.dawnfz.potcopyapi.service.abst.ParamsService;
import com.dawnfz.potcopyapi.wrapper.page.PageRequest;
import com.dawnfz.potcopyapi.wrapper.page.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 *  Type: Class
 *  Author: DawnFz.com
 *  Date: 2022/11/8 21:17
 *  Project: PotCopyApi
 *  Version: 1.0
 *  Describe: [TagServiceImpl类]
 */
@Service
@Transactional
public class ParamsServiceImpl implements ParamsService
{
    private final ParamsMapper paramsMapper;

    public ParamsServiceImpl(ParamsMapper paramsMapper)
    {
        this.paramsMapper = paramsMapper;
    }

    @Override
    public PageResult getTags(PageRequest pageRequest) throws SQLException
    {
        List<Tag> tags = paramsMapper.getTags();
        if (tags.size() == 0) tags = new ArrayList<>();
        return new PageResult(tags);
    }

    @Override
    public List<PotType> getPotTypes() throws SQLException
    {
        List<PotType> potTypes = paramsMapper.getPotTypes();
        if (potTypes.size() == 0) potTypes = new ArrayList<>();
        return potTypes;
    }

    @Override
    public List<Block> getBlocks(Integer typeId) throws SQLException
    {
        List<Block> blocks = paramsMapper.getBlocks(typeId);
        if (blocks.size() == 0) blocks = new ArrayList<>();
        return blocks;
    }
}
