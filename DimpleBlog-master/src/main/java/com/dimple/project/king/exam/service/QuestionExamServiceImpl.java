package com.dimple.project.king.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimple.project.king.exam.domain.QuestionExamEntity;
import com.dimple.project.king.exam.mapper.QuestionExamMapper;

/**
 * 考试
 * @author ls2008
 * @date 2019-11-29 21:59:27
 */
@Service
public class QuestionExamServiceImpl implements QuestionExamService{

	@Autowired
	private QuestionExamMapper mapper;

	@Override
	public List<QuestionExamEntity> pageList(Long userId) {
		return mapper.pageList(userId);
	}

	@Override
	public Long add(QuestionExamEntity questionExam) {
		mapper.insert(questionExam);
		return questionExam.getId();
	}
}