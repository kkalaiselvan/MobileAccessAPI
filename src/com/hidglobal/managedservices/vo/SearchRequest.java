package com.hidglobal.managedservices.vo;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {

	private List<String> schemas;
	private List<String> attributes;
	@JsonProperty("filter")
	private String filterList;
	private int totalResults;
	private int itemsPerPage;
	private int startIndex;
	private String sortBy;
	@JsonProperty("sortOrder")
	private String sortingOrder;
	private int count;
	
	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy.trim();
	}

	

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	public List<String> getAttributes() {

		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		
		List<String> searchAttributes = new ArrayList<String>();
		for (String attribute : attributes) {
			
			searchAttributes.add(attribute.trim());

		}

		this.attributes = searchAttributes;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSortingOrder() {
		return sortingOrder;
	}

	public void setSortingOrder(String sortingOrder) {
		this.sortingOrder = sortingOrder.trim();
	}

	public String getFilterList() {
		return filterList;
	}

	public void setFilterList(String filterList) {
		this.filterList = filterList;
	}

}
