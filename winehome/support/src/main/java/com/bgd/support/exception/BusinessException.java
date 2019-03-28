package com.bgd.support.exception;

/**
 * 
 * @ClassName:  BusinessException   
 * @Description: 业务异常 
 * @author: JackMore
 * @date:   2019年2月25日 下午2:09:03
 */
public class BusinessException extends BasicException {

	private static final long serialVersionUID = 1L;

	public BusinessException() {
		this(ExceptionEnum.业务异常.getMessage());
	}

	public BusinessException(String message) {
		super(message, ExceptionEnum.业务异常.getCode());
	}

}
