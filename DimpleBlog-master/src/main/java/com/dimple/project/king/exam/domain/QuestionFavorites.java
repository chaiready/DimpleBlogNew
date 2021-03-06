package com.dimple.project.king.exam.domain;

import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.SuperEntity;
import lombok.ToString;

@ToString
@TableName(value = "bg_question_favorites")
public class QuestionFavorites extends SuperEntity {

  /**
  * 
  */
  private static final long serialVersionUID = -8337547669294307179L;

  @TableId(value = "id", type = IdType.AUTO)
  @Id
  private Long id;

  private Long questionId;

  private Long userId;


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

}
