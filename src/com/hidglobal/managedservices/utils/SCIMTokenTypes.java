package com.hidglobal.managedservices.utils;

public enum SCIMTokenTypes {

	ATTR("Attribute Name"), EXPR_OP("operator"), VALUE("Attribute Value"), GRP_OP("and/or"), ATTR_NOTOP("Attribute Name/not");
	
	String tokenDescription;
	
	private SCIMTokenTypes(String tokenDescription) {
		this.tokenDescription = tokenDescription;
	}
	
	@Override
	public String toString() {
		return tokenDescription;
	}
	
}
