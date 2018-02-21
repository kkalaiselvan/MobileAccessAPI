package com.hidglobal.managedservices.vo;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Device extends Resource {
	private Long id;
	private String status;
	private String osVersion;
	private String secureElementType;
	private String manufacturer;
	private String model;
	private String applicationVersion;
	private String simOperator;
	private String bluetoothCapability;
	private String nfcCapability;
	@JsonIgnore
	private MobileId mobileId;
	@JsonProperty("urn:hid:scim:api:ma:1.0:Credential")
	private List<MobileId> mobileIds;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getSecureElementType() {
		return secureElementType;
	}

	public void setSecureElementType(String secureElementType) {
		this.secureElementType = secureElementType;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public String getSimOperator() {
		return simOperator;
	}

	public void setSimOperator(String simOperator) {
		this.simOperator = simOperator;
	}

	public String getBluetoothCapability() {
		return bluetoothCapability;
	}

	public void setBluetoothCapability(String bluetoothCapability) {
		this.bluetoothCapability = bluetoothCapability;
	}

	public String getNfcCapability() {
		return nfcCapability;
	}

	public void setNfcCapability(String nfcCapability) {
		this.nfcCapability = nfcCapability;
	}




	public List<MobileId> getMobileIds() {
		return mobileIds;
	}

	public void setMobileIds(List<MobileId> mobileIds) {
		this.mobileIds = mobileIds;
	}

	public MobileId getMobileId() {
		return mobileId;
	}

	public void setMobileId(MobileId mobileId) {
		this.mobileId = mobileId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



}
