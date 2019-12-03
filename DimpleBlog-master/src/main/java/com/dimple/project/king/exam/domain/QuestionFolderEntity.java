package com.dimple.project.king.exam.domain;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 问题集
 * 
 * @author ls2008
 * @date 2019-12-03 09:20:11
 */
@ToString
@TableName(value = "bg_question_folder")
public class QuestionFolderEntity extends SuperEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;// 表id

  private String folderName;// 问题夹名称

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }
}
