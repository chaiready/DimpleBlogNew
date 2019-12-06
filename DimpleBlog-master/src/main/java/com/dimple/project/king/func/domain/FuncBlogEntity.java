package com.dimple.project.king.func.domain;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;
import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 问题集
 * @author ls2008
 * @date 2019-12-06 17:44:11
 */
@ToString
@TableName(value = "bg_func_blog")
public class FuncBlogEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id

	private Long funcId;//菜单id

	private Long blogId;//用户id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFuncId() {
		return funcId;
	}

	public void setFuncId(Long funcId) {
		this.funcId = funcId;
	}

	public Long getBlogId() {
		return blogId;
	}

	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}

}