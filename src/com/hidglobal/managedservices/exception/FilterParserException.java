package com.hidglobal.managedservices.exception;



public class FilterParserException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorDetail;
	private int httpStatus;

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public FilterParserException(String errorDetail, String scimType, int httpStatus) {
		super();
		this.errorDetail = errorDetail;
		this.scimType = scimType;
		this.httpStatus = httpStatus;
		
	}

	/**
	 * @return the errorDetail
	 */
	public String getErrorDetail() {
		return errorDetail;
	}

	/**
	 * @param errorDetail the errorDetail to set
	 */
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}


}
