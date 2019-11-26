package com.dimple.project.king.exam;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dimple.project.king.exam.domain.QuestionOption;

/**
 * @className: HomeService
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
public interface QuestionOptionService {

	int insertObj(QuestionOption questionOption);
	

    List<QuestionOption> selectByQuestionIds(@Param("questionIds") Long[] questionIds);
}
