package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PacsUserActions {

	/**
	 * PacsUserActions provides operations which an user can select at the time
	 * on enrolling user.
	 */
	
	private String createInvitationCode;
	private String sendInvitationEmail;
	@JsonProperty("assignCredential")
	private String assignMobileId;
    private String partNumber;
    @JsonProperty("credential")
    private Long mobileId;
	public String getCreateInvitationCode() {
		return createInvitationCode;
	}

	public void setCreateInvitationCode(String createInvitationCode) {
		this.createInvitationCode = createInvitationCode.trim();
	}

	public String getSendInvitationEmail() {
		return sendInvitationEmail;
	}

	public void setSendInvitationEmail(String sendInvitationEmail) {
		this.sendInvitationEmail = sendInvitationEmail.trim();
	}

	
	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber.trim();
	}

	public String getAssignMobileId() {
		return assignMobileId;
	}

	public void setAssignMobileId(String assignMobileId) {
		this.assignMobileId = assignMobileId.trim();
	}

	public Long getMobileId() {
		return mobileId;
	}

	public void setMobileId(Long mobileId) {
		this.mobileId = mobileId;
	}

	
}
