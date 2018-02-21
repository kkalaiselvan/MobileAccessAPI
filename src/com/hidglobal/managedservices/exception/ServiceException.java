package com.hidglobal.managedservices.exception;

public class ServiceException extends Exception {

	
	private static final long serialVersionUID = 1L;
	String errmsg;
	int errcode;

	public ServiceException() {
		super();
		this.errmsg = "";
	}

	public ServiceException(String errmsg) {
		super();
		this.errmsg = errmsg;

	}
}
