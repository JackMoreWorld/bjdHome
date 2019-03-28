package com.bgd.support.exception;

import lombok.Data;

/**
 * 
 * @ClassName : BasicException
 * @Description: 基础异常，所有的异常实现均要继承该异常
 */
@Data
public class BasicException extends RuntimeException {
	private static final long serialVersionUID = -8694599150707362141L;



	private String code;
	private String message;
	private String data;


	public BasicException(String message, String errorCode) {
		this.code=errorCode;
		this.message=message;
		this.data=null;
	}


}
