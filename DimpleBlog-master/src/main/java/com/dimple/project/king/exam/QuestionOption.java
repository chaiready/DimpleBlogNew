package com.dimple.project.king.exam;


public class QuestionOption {

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
