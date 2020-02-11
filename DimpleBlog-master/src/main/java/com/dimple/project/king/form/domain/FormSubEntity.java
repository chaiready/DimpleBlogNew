package com.dimple.project.king.form.domain;

import lombok.ToString;
import com.dimple.framework.web.domain.SuperEntity;

import java.util.List;

import org.springframework.data.annotation.Id;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 表单
 * @author ls2008
 * @date 2020-02-03 11:00:35
 */
@ToString
@TableName(value = "bg_form_sub")
public class FormSubEntity extends SuperEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;//表id
	
	private Long mainid;//uuid

	private String colName;//列名

	private String colType;//列类型

	private String colVal;//列值
	
	@TableField(exist=false)
	private List<String> colVals;//列值

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public String getColVal() {
		return colVal;
	}

	public void setColVal(String colVal) {
		this.colVal = colVal;
	}

	public Long getMainid() {
		return mainid;
	}

	public void setMainid(Long mainid) {
		this.mainid = mainid;
	}

	public List<String> getColVals() {
		return colVals;
	}

	public void setColVals(List<String> colVals) {
		this.colVals = colVals;
	}


}