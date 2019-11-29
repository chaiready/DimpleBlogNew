package com.dimple.project.king.exam.service;

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

  List<Question> selectQuestionFavorites(Long userId);// 收藏的问题

  Question selectOne(Long valueOf);

  List<Question> selectQuestionWrong(Long userId);
}
