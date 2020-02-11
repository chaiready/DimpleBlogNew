package com.dimple.project.king.form.domain;

import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.SuperEntity;
import lombok.ToString;

/**
 * 表单数据
 * @author ls2008
 * @date 2020-02-03 11:00:35
 */
@ToString
@TableName(value = "bg_form_data")
public class FormDataEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id
	
	private Long mainid;//uuid

	private Long subid;//subid

	private String colData;//列数据
	
	private String rowuuid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMainid() {
		return mainid;
	}

	public void setMainid(Long mainid) {
		this.mainid = mainid;
	}

	public Long getSubid() {
		return subid;
	}

	public void setSubid(Long subid) {
		this.subid = subid;
	}

	public String getColData() {
		return colData;
	}

	public void setColData(String colData) {
		this.colData = colData;
	}

	public String getRowuuid() {
		return rowuuid;
	}

	public void setRowuuid(String rowuuid) {
		this.rowuuid = rowuuid;
	}

}