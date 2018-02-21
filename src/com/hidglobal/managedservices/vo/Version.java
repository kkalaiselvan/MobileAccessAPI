package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonIgnore;


public class Version {
	
	
	private String version;
	@JsonIgnore
	private String lastModifiedDt;
	public String getLastModifiedDt() {
		return lastModifiedDt;
	}

	public void setLastModifiedDt(String lastModifiedDt) {
		this.lastModifiedDt = lastModifiedDt;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}



}
