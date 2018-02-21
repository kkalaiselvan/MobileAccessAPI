package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PartNumbers extends Resource {
	@JsonProperty("urn:hid:scim:api:ma:1.0:PartNumber")
	private List<PartNumber> partNumbers;

	/**
	 * @return the partNumbers
	 */
	public List<PartNumber> getPartNumbers() {
		return partNumbers;
	}

	/**
	 * @param partNumbers
	 *            the partNumbers to set
	 */
	public void setPartNumbers(List<PartNumber> partNumbers) {
		this.partNumbers = partNumbers;
	}

}
