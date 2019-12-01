package com.dimple.project.king.exam.service;

import java.util.List;

import com.dimple.project.king.exam.domain.QuestionExamEntity;

/**
 * 考试
 * @author ls2008
 * @date 2019-11-29 21:59:27
 */
public interface QuestionExamService{

	List<QuestionExamEntity> pageList(Long userId);

	Long add(QuestionExamEntity questionExam);

}