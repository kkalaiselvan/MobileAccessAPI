package com.hidglobal.managedservices.exception;

public class MAValidationException extends BaseException {
	
	
	private static final long serialVersionUID = 1L;
	private int httpStatus;
	private String detail;

	public MAValidationException(String errorData, String detail,
			String errCode, String scimType, int httpStatus) {

		super(errorData, errCode, scimType);
		this.setDetail(detail);
		this.httpStatus = httpStatus;
		// TODO Auto-generated constructor stub
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}


}
