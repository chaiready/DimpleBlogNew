package com.dimple.project.king.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dimple.project.king.exam.domain.QuestionExamEntity;

/**
 * 考试
 * @author ls2008
 * @date 2019-11-29 21:59:27
 */
public interface QuestionExamMapper extends BaseMapper<QuestionExamEntity> {

	List<QuestionExamEntity> pageList(@Param("userId")Long userId,@Param("folderId")Long folderId);

}