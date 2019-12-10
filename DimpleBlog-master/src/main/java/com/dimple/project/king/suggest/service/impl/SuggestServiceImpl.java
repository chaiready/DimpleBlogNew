package com.dimple.project.king.suggest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.suggest.domain.SuggestEntity;
import com.dimple.project.king.suggest.service.SuggestService;
import com.dimple.project.king.suggest.mapper.SuggestMapper;


/**
 * 建议
 * @author ls2008
 * @date 2019-12-10 14:52:24
 */
@Service
public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, SuggestEntity> implements SuggestService{

	@Autowired
	private SuggestMapper mapper;
}