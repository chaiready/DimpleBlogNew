package com.dimple.project.king.exam.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.BaseEntity;

import lombok.ToString;

/**
 * @className: Menu
 * @description: 菜单权限表 bg_func
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@ToString
@TableName(value = "bg_question")
public class Question extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Id
	private Long id;

    private String type;
    
	private String questionOrder;

	private String content;

	private String answer;

	List<QuestionOption> optionList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(String questionOrder) {
		this.questionOrder = questionOrder;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public List<QuestionOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<QuestionOption> optionList) {
		this.optionList = optionList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
