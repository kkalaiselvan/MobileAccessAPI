package com.hidglobal.managedservices.exception;

public class MobileAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errmsg;
	int errcode;

	public MobileAccessException() {
		super();
		this.errmsg = "";
	}

	public MobileAccessException(String errmsg) {
		super();
		this.errmsg = errmsg;

	}

}
