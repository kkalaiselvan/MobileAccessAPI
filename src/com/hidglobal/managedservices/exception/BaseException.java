package com.hidglobal.managedservices.exception;

public class BaseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorData;
	String errCode;
	String scimType;
	
	public BaseException() {
		
	}

	public BaseException(String errorData, String errCode, String scimType) {
		super();
		this.errorData = errorData;
		this.errCode = errCode;
		this.scimType = scimType;
	}

	public String getScimType() {
		return scimType;
	}

	public void setScimType(String scimType) {
		this.scimType = scimType;
	}

	public String getErrorData() {
		return errorData;
	}

	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

}
