package com.dimple.project.king.exam.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
	public List<QuestionAnswer> selectByQuestionIds(Long userId, Long examId, Long[] questionIds) {
		return mapper.selectByQuestionIds(userId,examId, questionIds);
	}

	@Override
	public List<QuestionAnswer> selectByUidAndQid(Long userId, Long questionId) {
		Map<String, Object> columnMap = new HashMap<>(2);
		columnMap.put("user_id", userId);
		columnMap.put("question_id", questionId);
		return mapper.selectByMap(columnMap);
	}

	@Override
	public AjaxResult addAnswer(Long userId, Long examId, String questionOptionId) {
		String[] arr = questionOptionId.split("_");
		Long questionId = Long.valueOf(arr[0]);
		Long optionId = Long.valueOf(arr[1]);
		QuestionOption option = questionOptionService.selectOne(optionId);
		AjaxResult result = null;
		if (option == null) {
			return AjaxResult.error("选项不存在");
		}
		if (option.getQuestionId().longValue() != questionId.longValue()) {
			return AjaxResult.error("选项与题目不匹配");
		}
		int correct = QuestionAnswerEnum.wrong.getKey();
		Question question = questionService.selectOne(questionId);
		if (question.getAnswer().trim().equals(option.getOptionOrder().trim())) {
			correct = QuestionAnswerEnum.right.getKey();
		}
		Map<String, Object> columnMap = new HashMap<>(2);
		columnMap.put("user_id", userId);
		columnMap.put("question_id", questionId);
		// mapper.deleteByMap(columnMap);

		QueryWrapper<CollectionUtils> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("exam_id", examId);
		queryWrapper.eq("question_id", questionId);
		queryWrapper.orderByDesc("create_time");

		List<QuestionAnswer> answerList = mapper.selectList(new QueryWrapper<QuestionAnswer>().select("*").eq("user_id", userId).eq("exam_id", examId)
				.eq("question_id", questionId).orderByDesc("create_time"));

//		List<QuestionAnswer> answerList = mapper.selectByMap(columnMap);
		if (CollectionUtils.isEmpty(answerList)) {
			QuestionAnswer answer = new QuestionAnswer();
			answer.setQuestionId(questionId);
			answer.setOptionId(optionId);
			answer.setOptionOrder(option.getOptionOrder());
			answer.setCorrect(correct);
			answer.setUserId(userId);
			answer.setExamId(examId);
			answer.setCreateTime(new Date());
			mapper.insert(answer);
		} else {
			for(int i=0;i<answerList.size();i++){
				if(i==0){
					QuestionAnswer answer  = answerList.get(0);
					answer.setOptionId(optionId);
					answer.setOptionOrder(option.getOptionOrder());
					answer.setCorrect(correct);
					mapper.updateById(answer);
				}else{
					mapper.deleteById(answerList.get(i).getId());
				}
			}
		}

//		UpdateWrapper<QuestionAnswer> updateWrapper = new UpdateWrapper<>();
//		updateWrapper.eq("user_id", userId);
//		updateWrapper.eq("exam_id", examId);
//		updateWrapper.eq("question_id", questionId);
//		mapper.update(answer, updateWrapper);

		result = AjaxResult.success("保存成功");
		result.put("correct", correct);
		result.put("youAnswer", option.getOptionOrder());
		return result;
	}

}
