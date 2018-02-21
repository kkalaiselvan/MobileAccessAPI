package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchPartNumberResponse extends SearchResponse {
	@JsonProperty("Resources")
	private List<PartNumber> partNumbers;

	public List<PartNumber> getPartNumbers() {
		return partNumbers;
	}

	public void setPartNumbers(List<PartNumber> partNumbers) {
		this.partNumbers = partNumbers;
	}

	

}
