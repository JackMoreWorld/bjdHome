package com.bgd.support.exception;

public enum ExceptionEnum {
	响应正常("xx000", "响应正常"),
	未知异常("xx001", "未知异常"), 
	业务异常("xx002", "业务发生错误"), 
	参数异常("xx003", "参数错误"),
	验证异常("xx004", "连接失效，重新登陆");
	private String code;
	private String message;

	private ExceptionEnum(String errorCode, String errorMesg) {
		this.code = errorCode;
		this.message = errorMesg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



 
}
