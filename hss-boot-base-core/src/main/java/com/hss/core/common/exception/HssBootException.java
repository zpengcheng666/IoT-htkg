package com.hss.core.common.exception;

/**
 * @Description: jeecg-boot自定义异常
 * @author: jeecg-boot
 */
public class HssBootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HssBootException(String message){
		super(message);
	}
	
	public HssBootException(Throwable cause)
	{
		super(cause);
	}
	
	public HssBootException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
