package com.dimple.project.king.exam;

import java.util.List;

import com.dimple.project.king.exam.domain.Question;

/**
 * @className: HomeService
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
public interface QuestionService {

	int insertObj(Question question);
	
	List<Question> selectQuestion();
}
