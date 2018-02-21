package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Invitation extends Resource {

	@JsonIgnore
	private Long companyUserId;
	private String invitationCode;
	private String status;
	private String createdDate;
	private String expirationDate;
	@JsonProperty("id")
	private Long aamkInvitationId;

	
	public String getStatus() {
		return status;
	}
	
	public void setCompanyUserId(Long companyUserId) {
		this.companyUserId = companyUserId;
	}

	public Long getCompanyUserId() {
		return companyUserId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getAamkInvitationId() {
		return aamkInvitationId;
	}

	public void setAamkInvitationId(Long aamkInvitationId) {
		this.aamkInvitationId = aamkInvitationId;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

}
