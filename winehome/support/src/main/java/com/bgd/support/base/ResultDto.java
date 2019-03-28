package com.bgd.support.base;

import com.alibaba.fastjson.JSONObject;

public class ResultDto<T> {

    public static final Integer CODE_SUCC = 200;
    public static final Integer CODE_BUZ_ERROR = 400;

    private Integer code;
    private String message;
    private T data;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public ResultDto() {
		super();
	}
	public ResultDto(Integer code, String message, T data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public JSONObject toJSON() {
		JSONObject rpInfo = new JSONObject();
		rpInfo.put("code", getCode());
		rpInfo.put("Message", getMessage());
		rpInfo.put("Data", getData());
		return rpInfo;
	}
	
}
