package com.hidglobal.managedservices.utils;

public class SCIMFilterExpression {

	private SCIMExpressionType expressionType;
	private String fieldName;
	private SCIMExprOperators operator;
	private SCIMGroupOperators groupOperator;
	private String fieldValue;
	private boolean isComplete;
	
	public SCIMFilterExpression() {

	}
	
	public SCIMFilterExpression(SCIMExpressionType expressionType) {
		this.expressionType = expressionType;
	}

	public SCIMExpressionType getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(SCIMExpressionType expressionType) {
		this.expressionType = expressionType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public SCIMExprOperators getOperator() {
		return operator;
	}

	public void setOperator(SCIMExprOperators operator) {
		this.operator = operator;
	}

	public SCIMGroupOperators getGroupOperator() {
		return groupOperator;
	}

	public void setGroupOperator(SCIMGroupOperators groupOperator) {
		this.groupOperator = groupOperator;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	
}
