package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMobileAction extends Resource {
	public PacsUserActions getUserActions() {
		return userActions;
	}

	public void setUserActions(PacsUserActions userActions) {
		this.userActions = userActions;
	}

	@JsonProperty("urn:hid:scim:api:ma:1.0:UserAction")
	private PacsUserActions userActions;
}
