package com.hidglobal.managedservices.vo;


import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserInvitations extends Resource {
@JsonProperty("urn:hid:scim:api:ma:1.0:UserInvitation")
private List<Invitation> invitations;
@JsonProperty("urn:hid:scim:api:ma:1.0:Credential")
private List<MobileId> mobileId;
public List<Invitation> getInvitations() {
	return invitations;
}
public void setInvitations(List<Invitation> invitations) {
	this.invitations = invitations;
}
public List<MobileId> getMobileId() {
	return mobileId;
}
public void setMobileId(List<MobileId> mobileId) {
	this.mobileId = mobileId;
}

}
