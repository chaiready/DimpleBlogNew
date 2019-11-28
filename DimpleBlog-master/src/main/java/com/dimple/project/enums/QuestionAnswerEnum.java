package com.dimple.project.enums;


public enum QuestionAnswerEnum {

	not_Done(-1,"未答"),
	wrong(0,"错误"),
	right(1,"正确");
	
	private int key;
	private String msg;
	
	QuestionAnswerEnum(int key, String msg) {
		this.key = key;
		this.msg = msg;
	}
	
	public int getKey() {
		return key;
	}
	public String getMsg() {
		return msg;
	}
	
	
}
