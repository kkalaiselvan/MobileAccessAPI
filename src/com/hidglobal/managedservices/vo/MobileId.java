package com.hidglobal.managedservices.vo;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MobileId extends Resource {
	private Long id;
	private String partNumber;
	private String partnumberFriendlyName;
	private String cardNumber;
	private String referenceNumber;
	private String status;
	@JsonProperty("credentialType")
	private String credentialIdType;

	@JsonIgnore
	private Long endpointId;
	
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartnumberFriendlyName() {
		return partnumberFriendlyName;
	}

	public void setPartnumberFriendlyName(String partnumberFriendlyName) {
		this.partnumberFriendlyName = partnumberFriendlyName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}



	
	public String getCredentialIdType() {
		return credentialIdType;
	}

	public void setCredentialIdType(String credentialIdType) {
		this.credentialIdType = credentialIdType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
