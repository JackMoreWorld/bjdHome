package com.bgd.support.exception;
/**
 *
 * @ClassName:  BusinessException
 * @Description: 参数异常
 * @author: JackMore
 * @date:   2019年2月25日 下午2:09:03
 */
public class ParameterException extends BasicException {

	private static final long serialVersionUID = 1L;

	public ParameterException() {
		this(ExceptionEnum.参数异常.getMessage());
	}

	public ParameterException(String message) {
		super(message, ExceptionEnum.参数异常.getCode());
	}

}
