package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserDevices extends Resource {
	@JsonProperty("urn:hid:scim:api:ma:1.0:CredentialContainer")
	private List<Device> devices;

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

}
