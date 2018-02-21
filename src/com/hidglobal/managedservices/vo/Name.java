package com.hidglobal.managedservices.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Name {
		
	@NotNull(message = "{invalid.familyName.size}" )
	@Size(min = 2, max = 100, message = "{invalid.familyName.size}")
	@Pattern(regexp = "^[^\\^%<>#!@?=+{}\\[\\]|\\$;*]*$", message = "{invalid.familyName}")
	
	
	private String familyName;
	
	@NotNull(message = "{invalid.givenName.size}" )
	@Size(min = 2, max = 100, message = "{invalid.givenName.size}")
	@Pattern(regexp = "^[^\\^%<>#!@?=+{}\\[\\]|\\$;*]*$", message = "{invalid.givenName}")

	private String givenName;
	
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		if(familyName!=null)
		this.familyName = familyName.trim();
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		if(givenName!=null)
		this.givenName = givenName.trim();
	}
}
