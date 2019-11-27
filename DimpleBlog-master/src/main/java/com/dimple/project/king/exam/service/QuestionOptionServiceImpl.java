package com.dimple.project.king.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimple.project.king.exam.domain.QuestionOption;
import com.dimple.project.king.exam.mapper.QuestionOptionMapper;

/**
 * @className: HomeServiceImpl
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
@Service
public class QuestionOptionServiceImpl implements QuestionOptionService {

    @Autowired
    QuestionOptionMapper questionOptionMapper;

	@Override
	public int insertObj(QuestionOption questionOption) {
		return questionOptionMapper.insertObj(questionOption);
	}

	@Override
	public List<QuestionOption> selectByQuestionIds(Long[] questionIds) {
		return questionOptionMapper.selectByQuestionIds(questionIds);
	}



}
