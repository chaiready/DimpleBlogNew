package com.dimple.project.king.exam.domain;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 问题集
 * @author ls2008
 * @date 2019-12-04 09:25:29
 */
@ToString
@TableName(value = "bg_question_item")
public class QuestionItemEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id

	private Long folderId;//问题夹id

	private Long questionId;//用户id
	
	private String questionOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

  public String getQuestionOrder() {
    return questionOrder;
  }

  public void setQuestionOrder(String questionOrder) {
    this.questionOrder = questionOrder;
  }

}