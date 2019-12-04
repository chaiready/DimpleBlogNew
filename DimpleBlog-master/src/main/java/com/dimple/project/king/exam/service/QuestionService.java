package com.dimple.project.king.exam.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dimple.project.king.exam.domain.Question;

/**
 * @className: HomeService
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
public interface QuestionService extends IService<Question>{

  List<Question> selectQuestionByFolderId(Long folderId);

  List<Question> selectQuestionFavorites(Long userId,Long folderId);// 收藏的问题

  List<Question> selectQuestionWrong(Long userId,Long folderId);
}
