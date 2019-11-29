package com.dimple.project.coding;

/**
 * 成员变量属性
 * @ClassName: EntityInfo
 * @author liusheng
 * @date 2017年6月28日 下午2:43:12
 */
public class ColInfo {

	public class EleType {
		public static final String TEXT = "text";//
		public static final String DATE = "date";//
		public static final String DATETIME = "datetime";//
		public static final String SELECT = "select";//
		public static final String TEXTAREA = "textarea";//
		public static final String REF = "ref";//
	}
	
	private String colName;
	
	private String colDbName;
	
	private String colType;
	
	private Integer colLenth;
	
	private String colAnno;//注释
	
	private boolean isRequired=false;//是否必填
	
	private boolean isRow=false;//是否单独占一行
	
	private String eleType="text";//html元素
	private String enumGroup="BooleanType";//枚举类型
	
	private Integer colCount=1;//列数
	
	private boolean isSearch=false;//是否列表查询
	private boolean isListVisiable=true;//列表是否显示
	private boolean isDetailVisiable=true;//明细是否显示
	
	

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColDbName() {
		return colDbName;
	}

	public void setColDbName(String colDbName) {
		this.colDbName = colDbName;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public Integer getColLenth() {
		return colLenth;
	}

	public void setColLenth(Integer colLenth) {
		this.colLenth = colLenth;
	}

	public String getColAnno() {
		return colAnno;
	}

	public void setColAnno(String colAnno) {
		this.colAnno = colAnno;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isRow() {
		return isRow;
	}

	public void setRow(boolean isRow) {
		this.isRow = isRow;
	}

	public String getEleType() {
		return eleType;
	}

	public void setEleType(String eleType) {
		this.eleType = eleType;
	}

	public String getEnumGroup() {
		return enumGroup;
	}

	public void setEnumGroup(String enumGroup) {
		this.enumGroup = enumGroup;
	}

	public Integer getColCount() {
		return colCount;
	}

	public void setColCount(Integer colCount) {
		this.colCount = colCount;
	}

	public boolean isListVisiable() {
		return isListVisiable;
	}

	public void setListVisiable(boolean isListVisiable) {
		this.isListVisiable = isListVisiable;
	}

	public boolean isDetailVisiable() {
		return isDetailVisiable;
	}

	public void setDetailVisiable(boolean isDetailVisiable) {
		this.isDetailVisiable = isDetailVisiable;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
	
}
