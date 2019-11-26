package com.dimple.project.king.exam;

import com.dimple.framework.web.domain.BaseEntity;

/**
 * @className: Menu
 * @description: 菜单权限表 bg_func
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
public class Question extends BaseEntity{
	
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Long id;
  
  private String content;
  
  private String answer;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
  
  
}
