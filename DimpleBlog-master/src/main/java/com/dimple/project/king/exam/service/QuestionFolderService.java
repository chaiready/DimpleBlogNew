package com.dimple.project.king.exam.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dimple.project.king.exam.domain.QuestionFolderEntity;

/**
 * 问题集
 * @author ls2008
 * @date 2019-12-03 09:16:45
 */
public interface QuestionFolderService extends IService<QuestionFolderEntity>{

  List<QuestionFolderEntity> selectList();

}