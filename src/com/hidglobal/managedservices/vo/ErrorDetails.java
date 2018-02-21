package com.hidglobal.managedservices.vo;

import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErrorDetails {

	private List<String> schemas;
	private String scimType;
	private String detail;
	private int status;



	public String getScimType() {
		return scimType;
	}

	public void setScimType(String scimType) {
		this.scimType = scimType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the schemas
	 */
	public List<String> getSchemas() {
		return schemas;
	}

	/**
	 * @param schemas the schemas to set
	 */
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

}
