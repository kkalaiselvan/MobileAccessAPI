package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchMobileIdResponse extends SearchResponse {
	@JsonProperty("Resources")
	private List<MobileId> mobileIds;

	public List<MobileId> getMobileIds() {
		return mobileIds;
	}

	public void setMobileIds(List<MobileId> mobileIds) {
		this.mobileIds = mobileIds;
	}



}
