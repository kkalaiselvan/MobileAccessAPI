package com.hidglobal.managedservices.vo;

public class PartNumber extends Resource {

	private Long id;
	private String partNumber;
	private String friendlyName;
	private String description;
	private Integer availableQty;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the availableQty
	 */
	public Integer getAvailableQty() {
		return availableQty;
	}

	/**
	 * @param availableQty the availableQty to set
	 */
	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}


}
