package com.hidglobal.managedservices.vo;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CallBackUrlRequest extends Resource {
	
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public CallBackRequest getCallBackURL() {
		return callBackURL;
	}

	public void setCallBackURL(CallBackRequest callBackURL) {
		this.callBackURL = callBackURL;
	}

	
	
	@JsonProperty("urn:hid:scim:api:ma:1.0:CallbackRegistration")
	private CallBackRequest callBackURL;
	 
	
}
