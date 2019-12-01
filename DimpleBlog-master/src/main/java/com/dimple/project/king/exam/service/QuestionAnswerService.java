package com.dimple.project.king.exam.service;

import java.util.List;

import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.king.exam.domain.QuestionAnswer;

/**
 * @className: HomeService
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
public interface QuestionAnswerService {

	int delObj(Long userId,Long questionId);
	
	List<QuestionAnswer> selectByUidAndQid(Long userId,Long questionId);

    List<QuestionAnswer> selectByQuestionIds(Long userId, Long examId, Long[] questionIds);

	AjaxResult addAnswer(Long userId,Long examId,String questionOptionId);
}
