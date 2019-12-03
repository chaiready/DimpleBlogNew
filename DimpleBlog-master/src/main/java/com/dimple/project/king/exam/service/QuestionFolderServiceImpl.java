package com.dimple.project.king.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dimple.project.king.exam.mapper.QuestionFolderMapper;


/**
 * 问题集
 * 
 * @author ls2008
 * @date 2019-12-03 09:16:45
 */
@Service
public class QuestionFolderServiceImpl implements QuestionFolderService {

  @Autowired
  private QuestionFolderMapper mapper;
}
