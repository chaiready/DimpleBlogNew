package com.dimple.project.king.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.domain.QuestionOption;
import com.dimple.project.king.exam.mapper.QuestionMapper;

/**
 * @className: HomeServiceImpl
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    private QuestionOptionService questionOptionService;

    @Override
	public int insertObj(Question question22) {
//    	List<Question> questionList = JSON.parseArray(str, Question.class);
//    	int i=1;
//    	int j = 1;
//    	for(Question question:questionList){
//    		question.setId(Long.valueOf(i));
//    		question.setQuestionOrder(i+"");
//    		question.setType("0");
//    		System.out.println(i+" 、"+question.getContent());
//    		List<QuestionOption> optionList = question.getOptionList();
//    		int questionId = questionMapper.insertObj(question);
//    		for(QuestionOption qo:optionList){
//    			System.out.println("  "+qo.getOptionOrder()+" "+qo.getOptionVal());
//    			qo.setId(Long.valueOf(j));
//    			qo.setQuestionId(Long.valueOf(question.getId()));
//    			questionOptionService.insertObj(qo);
//    			
//    			j++;
//    		}
//    		i++;
//    	}
		return 1;
	}
    
	@Override
	public List<Question> selectQuestion() {
		return questionMapper.selectQuestion();
	}

  @Override
  public List<Question> selectQuestionFavorites(Long userId) {
    return questionMapper.selectQuestionFavorites(userId);
  }


}
