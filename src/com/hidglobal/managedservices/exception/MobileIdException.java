package com.hidglobal.managedservices.exception;

public class MobileIdException extends BaseException {
	private int httpStatus;
	private String detail;
	private static final long serialVersionUID = 1L;
	String errmsg;
	int errcode;
	public MobileIdException(String errorData, String detail,
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
