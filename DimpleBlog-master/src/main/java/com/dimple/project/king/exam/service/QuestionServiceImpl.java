package com.dimple.project.king.exam.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.enums.QuestionAnswerEnum;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.mapper.QuestionMapper;

/**
 * @className: HomeServiceImpl
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

  @Autowired
  QuestionMapper questionMapper;


  @Override
  public List<Question> selectQuestionByFolderId(Long folderId) {
    return questionMapper.selectQuestionByFolderId(folderId);
  }

  @Override
  public List<Question> selectQuestionFavorites(Long userId, Long folderId) {
    return questionMapper.selectQuestionFavorites(userId, folderId);
  }


  @Override
  public List<Question> selectQuestionWrong(Long userId, Long folderId) {
    return questionMapper.selectQuestionByAnwserCorrect(userId, folderId,
        QuestionAnswerEnum.wrong.getKey());
  }


}
