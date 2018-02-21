package com.hidglobal.managedservices.utils;

public enum SCIMExprOperators {

	EQ("eq"), NE("ne"), CO("co"), SW("sw"), EW("ew"), PR("pr"), GT("gt"), GE("ge"), LT("lt"), LE("le");
	
	private String operand;
	
	private SCIMExprOperators(String operand) {
		
		this.operand = operand;
	}

	@Override
	public String toString() {
		return operand;
	}
}
