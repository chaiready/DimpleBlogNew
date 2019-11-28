package com.dimple.project.king.exam.domain;

import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.BaseEntity2;
import lombok.ToString;

@ToString
@TableName(value = "bg_question_option")
public class QuestionOption  extends BaseEntity2{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7803458451105024714L;

	@TableId(value = "id", type = IdType.AUTO)
	@Id
	private Long id;

	private Long questionId;

	private String optionOrder;

	private String optionVal;
	
	@TableField(exist = false)
	private int correct = -1;//是否正確 QuestionAnswerEnum

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

    public int getCorrect() {
      return correct;
    }
  
    public void setCorrect(int correct) {
      this.correct = correct;
    }

}
