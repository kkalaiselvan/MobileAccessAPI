package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchInvitationResponse extends SearchResponse {
	@JsonProperty("Resources")
	private List<Invitation> invitations;

	public List<Invitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(List<Invitation> invitations) {
		this.invitations = invitations;
	}

}
