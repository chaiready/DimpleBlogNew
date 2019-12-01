package com.dimple.project.king.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dimple.project.king.exam.domain.QuestionAnswer;

/**
 * @className: BlogMapper
 * @description: 博客处理dao层
 * @auther: Dimple
 * @Date: 2019/3/16
 * @Version: 1.0
 */
public interface QuestionAnswerMapper  extends BaseMapper<QuestionAnswer> {
	
    List<QuestionAnswer> selectByQuestionIds(@Param("userId")Long userId,@Param("examId")Long examId,@Param("questionIds") Long[] questionIds);
}
