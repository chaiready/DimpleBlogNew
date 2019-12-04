package com.dimple.project.king.exam.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dimple.project.king.exam.domain.QuestionExamEntity;

/**
 * 考试
 * @author ls2008
 * @date 2019-11-29 21:59:27
 */
public interface QuestionExamService extends IService<QuestionExamEntity>{

	List<QuestionExamEntity> pageList(Long userId,Long folderId);

	Long add(QuestionExamEntity questionExam);

}