package com.dimple.project.king.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dimple.project.king.exam.domain.QuestionFavorites;
import com.dimple.project.king.exam.mapper.QuestionFavoritesMapper;

/**
 * @className: HomeServiceImpl
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
@Service
public class QuestionFavoritesServiceImpl implements QuestionFavoritesService {

  @Autowired
  QuestionFavoritesMapper mapper;

  @Override
  public int insertObj(QuestionFavorites obj) {
    return mapper.insertObj(obj);
  }


  @Override
  public int delObj(Long userId, Long questionId) {
    Map<String, Object> columnMap = new HashMap<>(2);
    columnMap.put("user_id", userId);
    columnMap.put("question_id", questionId);
    return mapper.deleteByMap(columnMap);
  }

  @Override
  public List<QuestionFavorites> selectByQuestionIds(Long userId, Long[] questionIds) {
    return mapper.selectByQuestionIds(userId, questionIds);
  }


  @Override
  public List<QuestionFavorites> selectByUidAndQid(Long userId, Long questionId) {
    Map<String, Object> columnMap = new HashMap<>(2);
    columnMap.put("user_id", userId);
    columnMap.put("question_id", questionId);
    return mapper.selectByMap(columnMap);
  }



}
