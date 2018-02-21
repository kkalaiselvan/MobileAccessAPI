package com.hidglobal.managedservices.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.exception.FilterParserException;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchFilter;

@Component
public class SCIMFiltertoSQL {

	@Autowired
	@Qualifier("pacsUserAttrToSqlColumnMap")
	private Properties pacsUserAttrToSqlColumnMap;

	@Autowired
	@Qualifier("invitationAttrToSqlColumnMap")
	private Properties invitationAttrToSqlColumnMap;

	@Autowired
	@Qualifier("partNoAttrToSqlColumnMap")
	private Properties partNoAttrToSqlColumnMap;

	@Autowired
	@Qualifier("mobileIdAttrToSqlColumnMap")
	private Properties mobileIdAttrToSqlColumnMap;

	@Autowired
	@Qualifier("scimOperatorsSqlColumnMap")
	private Properties scimOpsSqlOpMap;

	@Autowired
	@Qualifier("searchAttributes")
	private Properties searchAttribute;
	
	@Autowired
	@Qualifier("columnSQlFunctions")
	private Properties searchSQlFunctionsAttributeMap;
	
	@Autowired
	private MAUtils maUtils;

	public SearchFilter translateFilter(
			List<SCIMFilterExpression> filterExpressionList, Schemas schema)
			throws FilterParserException {

		StringBuilder translatedFilter = new StringBuilder();
		SearchFilter searchFilterBean = new SearchFilter();
		List<Object> params = new ArrayList<Object>();
		StringBuilder value = null;

		Properties scimAttrToSqlColumnMap = null;
		Properties SQlFunctionsAttributeMap = searchSQlFunctionsAttributeMap;
		
		if (schema == Schemas.PACSUSER) {
			for (int i = 0; i < filterExpressionList.size(); i++) {
				if (filterExpressionList.get(i).getFieldName() != null) {
					if (!(filterExpressionList.get(i).getFieldName()
							.toLowerCase().equals(searchAttribute
							.getProperty("fname")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("gname")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("email")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("emails")))		
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("externalId")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("userstatus")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("search.lastmodifieddt")))) {

						throw new FilterParserException(
								String.format(
										"Exception while parsing filter, attribute name [%s] is not supported.",
										filterExpressionList.get(i)
												.getFieldName().toLowerCase()),
								HIDContants.INVALIDSYNTAX,
								Response.Status.BAD_REQUEST.getStatusCode());
					}

				}
			}
			scimAttrToSqlColumnMap = pacsUserAttrToSqlColumnMap;
		} else if (schema == Schemas.INVITATIONS) {
			for (int i = 0; i < filterExpressionList.size(); i++) {
				if (filterExpressionList.get(i).getFieldName() != null) {
					if (!(filterExpressionList.get(i).getFieldName()
							.toLowerCase().equals(searchAttribute
							.getProperty("invitationid")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("invitationcode")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("invitationstatus")))
									&& !(filterExpressionList.get(i).getFieldName()
											.toLowerCase().equals(searchAttribute
											.getProperty("search.lastmodifieddt")))){

						throw new FilterParserException(
								String.format(
										"Exception while parsing filter, attribute name [%s] is not supported.",
										filterExpressionList.get(i)
												.getFieldName().toLowerCase()),
								HIDContants.INVALIDSYNTAX,
								Response.Status.BAD_REQUEST.getStatusCode());
					}

				}
			}
			scimAttrToSqlColumnMap = invitationAttrToSqlColumnMap;
		} else if (schema == Schemas.PART_NUMBER) {

			for (int i = 0; i < filterExpressionList.size(); i++) {
				if (filterExpressionList.get(i).getFieldName() != null) {
					if (!(filterExpressionList.get(i).getFieldName()
							.toLowerCase().equals(searchAttribute
							.getProperty("partnumber")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("friendlyname")))
									&& !(filterExpressionList.get(i).getFieldName()
											.toLowerCase().equals(searchAttribute
											.getProperty("search.lastmodifieddt")))) {

						throw new FilterParserException(
								String.format(
										"Exception while parsing filter, attribute name [%s] is not supported.",
										filterExpressionList.get(i)
												.getFieldName().toLowerCase()),
								HIDContants.INVALIDSYNTAX,
								Response.Status.BAD_REQUEST.getStatusCode());
					}

				}
			}
			scimAttrToSqlColumnMap = partNoAttrToSqlColumnMap;
		} else if (schema == Schemas.MOBILE_ID) {
			for (int i = 0; i < filterExpressionList.size(); i++) {
				if (filterExpressionList.get(i).getFieldName() != null) {
					if (!(filterExpressionList.get(i).getFieldName()
							.toLowerCase().equals(searchAttribute
							.getProperty("cardnumber")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("referencenumber")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("credentialstatus")))
							&& !(filterExpressionList.get(i).getFieldName()
									.toLowerCase().equals(searchAttribute
									.getProperty("search.lastmodifieddt")))) {

						throw new FilterParserException(
								String.format(
										"Exception while parsing filter, attribute name [%s] is not supported.",
										filterExpressionList.get(i)
												.getFieldName().toLowerCase()),
								HIDContants.INVALIDSYNTAX,
								Response.Status.BAD_REQUEST.getStatusCode());
					}

				}
			}
			scimAttrToSqlColumnMap = mobileIdAttrToSqlColumnMap;
		}

		for (SCIMFilterExpression expression : filterExpressionList) {
			value = new StringBuilder();

			if (expression.getExpressionType() == SCIMExpressionType.THREEPARTEXPR) {

				if (expression.getOperator() == SCIMExprOperators.SW) {
					value = value.append(expression.getFieldValue())
							.append("%");
				} else if (expression.getOperator() == SCIMExprOperators.EW) {
					value = value.append("%")
							.append(expression.getFieldValue());
				} else if (expression.getOperator() == SCIMExprOperators.CO) {
					value = value.append("%")
							.append(expression.getFieldValue()).append("%");
				} else {
					value = value.append(expression.getFieldValue());
				}

				if (!scimAttrToSqlColumnMap.containsKey(expression
						.getFieldName().toLowerCase())) {

					throw new FilterParserException(
							String.format(
									"Exception while parsing filter, attribute name [%s] is invalid.",
									expression.getFieldName()),
							HIDContants.INVALIDSYNTAX,
							Response.Status.BAD_REQUEST.getStatusCode());
				}

				if (!scimOpsSqlOpMap.containsKey(expression.getOperator()
						.toString())) {

					throw new FilterParserException(
							String.format(
									"Exception while parsing filter, operator [%s] is invalid.",
									expression.getOperator().toString()),
							HIDContants.INVALIDSYNTAX,
							Response.Status.BAD_REQUEST.getStatusCode());

				}

				
				//start
				if(expression.getFieldName().toLowerCase().equals("meta.lastmodified")){
					 
					if(maUtils.isValidDateTime(value.toString())){
						
						String timeZone = maUtils.getTimeZone(value.toString());
						if(timeZone == null){
							value.append("Z");
						}
					}
					else{
						throw new FilterParserException(
								String.format(
										"Exception while parsing filter,invalid date format.",
										expression.getFieldName()),
								HIDContants.INVALIDSYNTAX,
								Response.Status.BAD_REQUEST.getStatusCode());
					}		
					
					translatedFilter
					.append(scimAttrToSqlColumnMap.getProperty(expression
							.getFieldName().toLowerCase()))
					.append(" ")
					.append(scimOpsSqlOpMap.getProperty(expression
							.getOperator().toString())).append(" ")
					.append(SQlFunctionsAttributeMap.getProperty("meta.lastmodified")).append(" ");
				}//end
				else{
					translatedFilter
							.append(scimAttrToSqlColumnMap.getProperty(expression
									.getFieldName().toLowerCase()))
							.append(" ")
							.append(scimOpsSqlOpMap.getProperty(expression
									.getOperator().toString())).append(" ")
							.append("?").append(" ");
				}


				params.add(value.toString());

			} else if (expression.getExpressionType() == SCIMExpressionType.TWOPARTEXPR) {
				String rightParenthesis = "";
				//start
				if(expression.getFieldName().toLowerCase().equals("meta.lastmodified")){
					
					if(expression.getOperator().toString().equals(SCIMExprOperators.PR.toString())){
						rightParenthesis = ")";
					}
				}
				//end
				translatedFilter
						.append(scimAttrToSqlColumnMap.getProperty(expression
								.getFieldName().toLowerCase()))
						.append(" ")
						.append(scimOpsSqlOpMap.getProperty(expression
								.getOperator().toString())).append(rightParenthesis);

			} else if (expression.getExpressionType() == SCIMExpressionType.GROUP_OP) {
				translatedFilter.append(
						expression.getGroupOperator().toString()).append(" ");
			} else if (expression.getExpressionType() == SCIMExpressionType.LPAR) {
				translatedFilter.append("( ");
			} else if (expression.getExpressionType() == SCIMExpressionType.RPAR) {
				translatedFilter.append(") ");
			}
		}

		searchFilterBean.setWhereClause(translatedFilter.toString());
		searchFilterBean.setSearchParams(params);

		return searchFilterBean;

	}

	public Properties getPacsUserAttrToSqlColumnMap() {
		return pacsUserAttrToSqlColumnMap;
	}

	public void setPacsUserAttrToSqlColumnMap(
			Properties pacsUserAttrToSqlColumnMap) {
		this.pacsUserAttrToSqlColumnMap = pacsUserAttrToSqlColumnMap;
	}

	public Properties getInvitationAttrToSqlColumnMap() {
		return invitationAttrToSqlColumnMap;
	}

	public void setInvitationAttrToSqlColumnMap(
			Properties invitationAttrToSqlColumnMap) {
		this.invitationAttrToSqlColumnMap = invitationAttrToSqlColumnMap;
	}

	public Properties getScimOpsSqlOpMap() {
		return scimOpsSqlOpMap;
	}

	public void setScimOpsSqlOpMap(Properties scimOpsSqlOpMap) {
		this.scimOpsSqlOpMap = scimOpsSqlOpMap;
	}

}
