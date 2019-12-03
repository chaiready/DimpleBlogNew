package com.dimple.project.king.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dimple.project.king.exam.mapper.QuestionItemMapper;


/**
 * 问题集
 * @author ls2008
 * @date 2019-12-03 16:47:17
 */
@Service
public class QuestionItemServiceImpl implements QuestionItemService{

	@Autowired
	private QuestionItemMapper mapper;
}