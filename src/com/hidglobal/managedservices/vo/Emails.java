package com.hidglobal.managedservices.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



public class Emails {
	
	@Pattern(regexp = "[A-Za-z0-9][A-Za-z0-9._%+-]*@[A-Za-z0-9][A-Za-z0-9.-]*[.]{1}[A-Za-z]{2,63}", message = "{invalid.email.value}")
	@NotNull(message = "{invalid.email.value}")
	@Size(min = 2, message = "{invalid.email.value}")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if(value!=null)
		this.value = value.trim();
	}
}
