package com.hss.core.common.exception;

/**
 * @Description: jeecg-boot自定义401异常
 * @author: jeecg-boot
 */
public class HssBoot401Exception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HssBoot401Exception(String message){
		super(message);
	}

	public HssBoot401Exception(Throwable cause)
	{
		super(cause);
	}

	public HssBoot401Exception(String message, Throwable cause)
	{
		super(message,cause);
	}
}
