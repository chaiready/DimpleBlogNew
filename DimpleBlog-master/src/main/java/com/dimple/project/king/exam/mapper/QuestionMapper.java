package com.dimple.project.king.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dimple.project.king.exam.domain.Question;

/**
 * @className: BlogMapper
 * @description: 博客处理dao层
 * @auther: Dimple
 * @Date: 2019/3/16
 * @Version: 1.0
 */
public interface QuestionMapper extends BaseMapper<Question> {

  int insertObj(Question question);

  List<Question> selectQuestionByFolderId(@Param("folderId")Long folderId);

  List<Question> selectQuestionFavorites(Long userId,Long folderId);

  List<Question> selectQuestionByAnwserCorrect(@Param("userId")Long userId,Long folderId,@Param("correct")int correct);
}
