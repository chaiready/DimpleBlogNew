package com.dimple.project.king.exam.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.exam.domain.QuestionFolderEntity;
import com.dimple.project.king.exam.mapper.QuestionFolderMapper;


/**
 * 问题集
 * 
 * @author ls2008
 * @date 2019-12-03 09:16:45
 */
@Service
public class QuestionFolderServiceImpl extends ServiceImpl<QuestionFolderMapper, QuestionFolderEntity> implements QuestionFolderService {

  @Autowired
  private QuestionFolderMapper mapper;

  @Override
  public List<QuestionFolderEntity> selectList() {
    return mapper.selectList(null);
  }
}
