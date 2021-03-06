package com.dimple.project.king.exam.domain;

import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.SuperEntity;
import lombok.ToString;

@ToString
@TableName(value = "bg_question_answer")
public class QuestionAnswer extends SuperEntity {

	/**
	* 
	*/
	private static final long serialVersionUID = -8337547669294307179L;

	@TableId(value = "id", type = IdType.AUTO)
	@Id
	private Long id;

	private Long questionId;

	private Long optionId;

	private String optionOrder;

	private Long userId;

	private int correct;// 是否正確

	private Long examId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public String getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(String optionOrder) {
		this.optionOrder = optionOrder;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

}
