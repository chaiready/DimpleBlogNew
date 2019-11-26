package com.dimple.project.king.exam.domain;

import org.springframework.data.annotation.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.BaseEntity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "bg_question_option")
public class QuestionOption  extends BaseEntity{

	@TableId(value = "id", type = IdType.AUTO)
	@Id
	private Long id;

	private Long questionId;

	private String optionOrder;

	private String optionVal;

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

	public String getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(String optionOrder) {
		this.optionOrder = optionOrder;
	}

	public String getOptionVal() {
		return optionVal;
	}

	public void setOptionVal(String optionVal) {
		this.optionVal = optionVal;
	}

}
