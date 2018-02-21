package com.hidglobal.managedservices.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CallBackRequest {

	@JsonIgnore
	private String lastModifiedDt;
	public String getLastModifiedDt() {
		return lastModifiedDt;
	}

	public void setLastModifiedDt(String lastModifiedDt) {
		this.lastModifiedDt = lastModifiedDt;
	}

	

	@JsonProperty("callbackUrl")
	@Valid
	@NotNull(message = "{invalid.url.required}")
	@NotEmpty(message = "{invalid.url.required}")
	@Size(min =1, message = "{invalid.url.required}")
	@Pattern(regexp="^(H|h)(t|T)(t|T)(p|P)(s|S):\\/\\/((\\S+:\\S+@[0-9a-zA-Z-\\.~_]+(:(\\d+($|\\/.*|))|($|\\/.*)))|([0-9a-zA-Z-\\.~_]+(:(\\d+($|\\/.*|))|($|\\/.*))))", message = "{invalid.url}")
	
	
	//@Pattern(regexp = "^https\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(\\S*)?$", message = "{invalid.url}")
	private String url;
	
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
