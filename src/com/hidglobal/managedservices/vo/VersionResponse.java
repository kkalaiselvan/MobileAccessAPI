package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class VersionResponse extends Resource{

	@JsonProperty("urn:hid:scim:api:ma:1.0:SDKVersion")
	private Version version;

	
	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
}
