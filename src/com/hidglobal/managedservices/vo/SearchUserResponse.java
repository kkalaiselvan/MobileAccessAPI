package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchUserResponse extends SearchResponse{
	@JsonProperty("Resources")
	private List<PacsUser> users;

	public List<PacsUser> getUsers() {
		return users;
	}

	public void setUsers(List<PacsUser> users) {
		this.users = users;
	}

}
