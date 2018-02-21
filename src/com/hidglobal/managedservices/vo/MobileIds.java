package com.hidglobal.managedservices.vo;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MobileIds extends Resource {
	@JsonProperty("urn:hid:scim:api:ma:1.0:Credential")
	private List<MobileId> mobileIds;

	/**
	 * @return the mobileIds
	 */
	public List<MobileId> getMobileIds() {
		return mobileIds;
	}

	/**
	 * @param mobileIds the mobileIds to set
	 */
	public void setMobileIds(List<MobileId> mobileIds) {
		this.mobileIds = mobileIds;
	}
}
