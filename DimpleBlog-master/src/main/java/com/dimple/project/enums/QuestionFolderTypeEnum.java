package com.dimple.project.enums;


public enum QuestionFolderTypeEnum {

	CHOICE("choice","选择题"),
	PDF("pdf","pdf格式");
	
	private String key;
	private String msg;
	
	QuestionFolderTypeEnum(String key, String msg) {
		this.key = key;
		this.msg = msg;
	}
	
	public String getKey() {
		return key;
	}
	public String getMsg() {
		return msg;
	}
	
	
}