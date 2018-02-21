package com.hidglobal.managedservices.vo;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PacsUser extends Resource {
	
	private Long id;
	private String externalId;
	@Valid
	@NotNull(message = "{invalid.name}")
	private Name name;
	@Valid
	@NotNull(message = "{invalid.email}")
	private List<Emails> emails;
	@JsonProperty("urn:hid:scim:api:ma:1.0:UserAction")
	private PacsUserActions userActions;
	@JsonProperty("urn:hid:scim:api:ma:1.0:UserInvitation")
	private List<Invitation> userInvitations;
	@JsonProperty("urn:hid:scim:api:ma:1.0:CredentialContainer")
	private List<Device> userDevices;
	@JsonProperty("urn:hid:scim:api:ma:1.0:Credential")
	private List<MobileId> usermobileIds;
	@JsonIgnore
	private String lastModifiedDt;
	private String status;
	public List<Invitation> getUserInvitations() {
		return userInvitations;
	}

	public void setUserInvitations(List<Invitation> userInvitations) {
		this.userInvitations = userInvitations;
	}

	public List<Device> getUserDevices() {
		return userDevices;
	}

	public void setUserDevices(List<Device> userDevices) {
		this.userDevices = userDevices;
	}


	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		if(externalId!=null)
		this.externalId = externalId.trim();
	}


	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public List<Emails> getEmails() {
		return emails;
	}

	public void setEmails(List<Emails> emails) {
		this.emails = emails;
	}

	public PacsUserActions getUserActions() {
		return userActions;
	}

	public void setUserActions(PacsUserActions userActions) {
		this.userActions = userActions;
	}

	public String getLastModifiedDt() {
		return lastModifiedDt;
	}

	public void setLastModifiedDt(String lastModifiedDt) {
		this.lastModifiedDt = lastModifiedDt.trim();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MobileId> getUsermobileIds() {
		return usermobileIds;
	}

	public void setUsermobileIds(List<MobileId> usermobileIds) {
		this.usermobileIds = usermobileIds;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		if(status!=null)	
		this.status = status.trim();
	}

}
