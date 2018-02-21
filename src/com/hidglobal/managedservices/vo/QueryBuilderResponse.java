package com.hidglobal.managedservices.vo;

import java.util.List;

public class QueryBuilderResponse {

	private String queryString;
	private String queryStringforCount;
	private List<Object> queryParams;
	private List<Object> countQueryParams;

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getQueryStringforCount() {
		return queryStringforCount;
	}

	public void setQueryStringforCount(String queryStringforCount) {
		this.queryStringforCount = queryStringforCount;
	}

	public List<Object> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(List<Object> queryParams) {
		this.queryParams = queryParams;
	}

	public List<Object> getCountQueryParams() {
		return countQueryParams;
	}

	public void setCountQueryParams(List<Object> countQueryParams) {
		this.countQueryParams = countQueryParams;
	}

}
