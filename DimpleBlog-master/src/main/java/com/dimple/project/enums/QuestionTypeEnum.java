package com.dimple.project.enums;


public enum QuestionTypeEnum {

    scq("scq","单选题"),//Single choice question
	mcq("mcq","多选题");//Multiple choice questions
	
	private String key;
	private String msg;
	
	QuestionTypeEnum(String key, String msg) {
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
