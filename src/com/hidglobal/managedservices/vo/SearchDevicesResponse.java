package com.hidglobal.managedservices.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchDevicesResponse extends SearchResponse {
	@JsonProperty("Resources")
	private List<Device> devices;

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}



}
