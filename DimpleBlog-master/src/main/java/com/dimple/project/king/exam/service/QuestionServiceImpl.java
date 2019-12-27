package com.dimple.project.king.exam.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.common.utils.text.Convert;
import com.dimple.project.enums.QuestionAnswerEnum;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

	@Autowired
	QuestionMapper questionMapper;
	@Autowired
	QuestionOptionService questionOptionService;

	@Override
	public List<Question> selectQuestionByFolderId(Long folderId) {
		return questionMapper.selectQuestionByFolderId(folderId);
	}

	@Override
	public List<Question> selectQuestionFavorites(Long userId, Long folderId) {
		return questionMapper.selectQuestionFavorites(userId, folderId);
	}

	@Override
	public List<Question> selectQuestionWrong(Long userId, Long folderId) {
		return questionMapper.selectQuestionByAnwserCorrect(userId, folderId, QuestionAnswerEnum.wrong.getKey());
	}

	/**
	 * 判断是否答案
	 * @param optionAnswer
	 * @param index
	 * @return
	 */
	private String checkIsAnswer(Integer []optionAnswer,int index){
		if(optionAnswer==null){
			return "0";
		}
		for(Integer oa:optionAnswer){
			if(oa==index){
				return "1";
			}
		}
		return "0";
	}
	
	@Transactional
	@Override
	public int saveQuestionOption(Question question, String[] optionVal,Integer []optionAnswer) {
		question.setCreateBy(ShiroUtils.getLoginName());
		question.setCreateTime(new Date());
		questionMapper.insert(question);
		int i=0;
		List<QuestionOption> optionList = new ArrayList<>();
		for(String ov:optionVal){
			QuestionOption option = new QuestionOption();
			option.setOptionVal(ov);
			option.setAnswer(checkIsAnswer(optionAnswer, i));
			i++;
			option.setOptionOrder(Convert.numberToLetter(i));
			option.setCreateBy(ShiroUtils.getLoginName());
			option.setCreateTime(new Date());
			option.setQuestionId(question.getId());
			optionList.add(option);
		}
		questionOptionService.saveBatch(optionList);
		return 1;
	}

	@Transactional
	@Override
	public int updateQuestionOption(Question question, Long[] optionId, String[] optionVal,Integer []optionAnswer, Long[] delOptinId) {
		if(delOptinId!=null){//删除选项
			questionOptionService.removeByIds(Arrays.asList(delOptinId));
		}
		List<QuestionOption> addOptionList = new ArrayList<>();
		List<QuestionOption> updateOptionList = new ArrayList<>();
		for(int i=0;i<optionId.length;i++){
			if(optionId[i]==0){//新增的
				QuestionOption option = new QuestionOption();
				option.setOptionVal(optionVal[i]);
				option.setAnswer(checkIsAnswer(optionAnswer, i));
				option.setOptionOrder(Convert.numberToLetter(i+1));
				option.setCreateBy(ShiroUtils.getLoginName());
				option.setCreateTime(new Date());
				option.setQuestionId(question.getId());
				addOptionList.add(option);
			}else{
				QuestionOption option = new QuestionOption();
				option.setId(optionId[i]);
				option.setOptionVal(optionVal[i]);
				option.setAnswer(checkIsAnswer(optionAnswer, i));
				option.setOptionOrder(Convert.numberToLetter(i+1));
				option.setUpdateBy(ShiroUtils.getLoginName());
				option.setUpdateTime(new Date());
				option.setQuestionId(question.getId());
				updateOptionList.add(option);
			}
		}
		questionOptionService.saveBatch(addOptionList);
		questionOptionService.updateBatchById(updateOptionList);
		return questionMapper.updateById(question);
	}

}
