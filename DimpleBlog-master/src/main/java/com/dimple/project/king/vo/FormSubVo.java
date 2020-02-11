package com.dimple.project.king.vo;

import java.util.List;

/**
 * 表单提交vo
 * @author lenovo
 *
 */
public class FormSubVo {

	private Long subId;
	
	private String colName;//列名

	private String colType;//列类型

	private List<String> colVals;//列值

	public Long getSubId() {
		return subId;
	}

	public void setSubId(Long subId) {
		this.subId = subId;
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

	public List<String> getColVals() {
		return colVals;
	}

	public void setColVals(List<String> colVals) {
		this.colVals = colVals;
	}
	
	
}
