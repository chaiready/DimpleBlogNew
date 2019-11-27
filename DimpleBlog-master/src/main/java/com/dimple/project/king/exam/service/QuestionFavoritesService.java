package com.dimple.project.king.exam.service;

import java.util.List;
import com.dimple.project.king.exam.domain.QuestionFavorites;

/**
 * @className: HomeService
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
public interface QuestionFavoritesService {

	int insertObj(QuestionFavorites obj);
	
	int delObj(Long userId,Long questionId);
	
	List<QuestionFavorites> selectByUidAndQid(Long userId,Long questionId);

    List<QuestionFavorites> selectByQuestionIds(Long userId, Long[] questionIds);
}
