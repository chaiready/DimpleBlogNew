package com.dimple.project.king.exam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.exam.domain.QuestionItemEntity;
import com.dimple.project.king.exam.mapper.QuestionItemMapper;
import com.dimple.project.king.exam.service.QuestionItemService;


/**
 * 问题集
 * @author ls2008
 * @date 2019-12-04 09:31:12
 */
@Service
public class QuestionItemServiceImpl extends ServiceImpl<QuestionItemMapper, QuestionItemEntity> implements QuestionItemService{

	@Autowired
	private QuestionItemMapper mapper;
}