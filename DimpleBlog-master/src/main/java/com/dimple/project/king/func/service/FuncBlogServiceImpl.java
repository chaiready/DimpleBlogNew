package com.dimple.project.king.func.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.func.domain.FuncBlogEntity;
import com.dimple.project.king.func.mapper.FuncBlogMapper;


/**
 * 问题集
 * @author ls2008
 * @date 2019-12-06 17:44:11
 */
@Service
public class FuncBlogServiceImpl extends ServiceImpl<FuncBlogMapper, FuncBlogEntity> implements FuncBlogService{

	@Autowired
	private FuncBlogMapper mapper;
}