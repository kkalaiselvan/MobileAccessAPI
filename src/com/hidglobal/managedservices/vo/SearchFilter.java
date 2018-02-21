package com.hidglobal.managedservices.vo;

import java.util.List;

public class SearchFilter {

	private String whereClause;
	
	private List<Object> searchParams;

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public List<Object> getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(List<Object> searchParams) {
		this.searchParams = searchParams;
	}


}
