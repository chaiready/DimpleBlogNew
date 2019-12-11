package com.dimple.project.king.userlog.domain;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 建议
 * @author ls2008
 * @date 2019-12-11 14:08:40
 */
@ToString
@TableName(value = "bg_user_log")
public class UserLogEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	public static String FORGET_PWD = "01";
	
	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id

	private String content;//内容

	private String logtype;//日志类型

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

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

}