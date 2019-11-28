package com.dimple.project.king.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dimple.framework.web.domain.AjaxResult;
import com.dimple.project.enums.QuestionAnswerEnum;
import com.dimple.project.king.exam.domain.Question;
import com.dimple.project.king.exam.domain.QuestionAnswer;
import com.dimple.project.king.exam.domain.QuestionOption;
import com.dimple.project.king.exam.mapper.QuestionAnswerMapper;

/**
 * @className: HomeServiceImpl
 * @description:
 * @auther: Dimple
 * @date: 03/27/19
 * @version: 1.0
 */
@Service
public class QuestionAnswerServiceImpl implements QuestionAnswerService {

  @Autowired
  QuestionAnswerMapper mapper;
  @Autowired
  QuestionService questionService;
  @Autowired
  QuestionOptionService questionOptionService;


  @Override
  public int delObj(Long userId, Long questionId) {
    Map<String, Object> columnMap = new HashMap<>(2);
    columnMap.put("user_id", userId);
    columnMap.put("question_id", questionId);
    return mapper.deleteByMap(columnMap);
  }

  @Override
  public List<QuestionAnswer> selectByQuestionIds(Long userId, Long[] questionIds) {
    return mapper.selectByQuestionIds(userId, questionIds);
  }


  @Override
  public List<QuestionAnswer> selectByUidAndQid(Long userId, Long questionId) {
    Map<String, Object> columnMap = new HashMap<>(2);
    columnMap.put("user_id", userId);
    columnMap.put("question_id", questionId);
    return mapper.selectByMap(columnMap);
  }

	@Override
	public AjaxResult addAnswer(Long userId,String questionOptionId) {
		String []arr = questionOptionId.split("_");
		Long questionId = Long.valueOf(arr[0]);
		Long optionId = Long.valueOf(arr[1]);
		QuestionOption option = questionOptionService.selectOne(optionId);
		AjaxResult result = null;
		if(option == null){
			return AjaxResult.error("选项不存在");
		}
		if(option.getQuestionId().longValue()!= questionId.longValue()){
			return AjaxResult.error("选项与题目不匹配");
		}
		int correct = QuestionAnswerEnum.wrong.getKey();
		Question question = questionService.selectOne(questionId);
		if(question.getAnswer().trim().equals(option.getOptionOrder().trim())){
			correct = QuestionAnswerEnum.right.getKey();
		}
	    Map<String, Object> columnMap = new HashMap<>(2);
	    columnMap.put("user_id", userId);
	    columnMap.put("question_id", questionId);
		mapper.deleteByMap(columnMap);
		QuestionAnswer answer = new QuestionAnswer();
		answer.setQuestionId(questionId);
		answer.setOptionId(optionId);
		answer.setOptionOrder(option.getOptionOrder());
		answer.setCorrect(correct);
		answer.setUserId(userId);
		mapper.insert(answer);
		result = AjaxResult.success("保存成功");
		result.put("correct", correct);
		result.put("youAnswer", option.getOptionOrder());
		return result;
	}



}
