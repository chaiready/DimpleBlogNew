package com.dimple.project.king.suggest.domain;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 建议
 * @author ls2008
 * @date 2019-12-10 14:52:24
 */
@ToString
@TableName(value = "bg_suggest")
public class SuggestEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id

	private String content;//内容

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

}