package com.hidglobal.managedservices.utils;

public enum SCIMGroupOperators {

	NOT("not"), AND("and"), OR("or");
	
	private String operand;
	
	private SCIMGroupOperators(String operand) {
		
		this.operand = operand;
	}

}
