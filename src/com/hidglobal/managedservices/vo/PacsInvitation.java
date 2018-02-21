package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PacsInvitation extends Resource{

	@JsonProperty("urn:hid:scim:api:ma:1.0:UserAction")
	private PacsUserActions userActions;
	
	public PacsUserActions getUserActions() {
		return userActions;
	}

	public void setUserActions(PacsUserActions userActions) {
		this.userActions = userActions;
	}
}
