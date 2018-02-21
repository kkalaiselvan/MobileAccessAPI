package com.hidglobal.managedservices.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.exception.FilterParserException;
import com.hidglobal.managedservices.vo.QueryBuilderResponse;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchFilter;

@Component
public class QueryBuilder {

	@Autowired
	@Qualifier("dbColumnMap")
	Properties dbColumnMap;

	@Autowired
	SearchFilterParser searchFilterParser;

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
	@Qualifier("deviceAttrToSqlColumnMap")
	private Properties deviceAttrToSqlColumnMap;

	@Autowired
	@Qualifier("deviceMobileIdAttrToSqlColumnMap")
	private Properties deviceMobileIdAttrToSqlColumnMap;

	Properties scimAttrToSqlColumnMap = null;
	// Query Formatter
	public static final String SELECT = " SELECT ";
	public static final String COUNT = " COUNT(*) ";
	public static final String QUERY_FROM = " FROM ";
	public static final String QUERY_FILTER = " WHERE ";
	public static final String SORTBY = " ORDER BY ";
	public static final String LOGICAL_OPERATOR_AND = " AND ";
	public static final String EQUALS = "=";

	// Query Builder Defaults
	public static final int START_INDEX = 1;
	public static final int ITEMS_PER_PAGE = 25;
	public static final String DESCENDING = "desc";
	public static final String ASCENDING = "asc";

	private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

	/**
	 * 
	 * SQL Query Builder to build SQL for listing below Entities
	 * User,Invitation, Device and Credential. This Method is supports Filters
	 * of Type JAVA Object
	 * 
	 * @param entity
	 * @param filters
	 * @return
	 */
	public QueryBuilderResponse sqlQueryBuilder(Schemas schema,
			QueryFilterId filters) {

		logger.info("Entered Query Builder");
		scimAttrToSqlColumnMap = getScimAttrToSqlColumnMap(schema);
		QueryBuilderResponse builderResponse = new QueryBuilderResponse();
		List<Object> queryParams = new ArrayList<Object>();
		String queryAttributes = getQueryAttributes();
		String queryTable = getQueryTable();
		String queryFilters = getQueryFilterById(builderResponse, schema,
				filters, queryParams);
		String sortBy = getDefaultSortOrder();

		String sqlQuery = stringBuilder(SELECT, queryAttributes, QUERY_FROM,
				queryTable, QUERY_FILTER, queryFilters, sortBy);
		String sqlQueryCount = stringBuilder(SELECT, COUNT, QUERY_FROM,
				queryTable, QUERY_FILTER, queryFilters, sortBy);

		String listTopNQuery = paginate(builderResponse, sqlQuery, START_INDEX,
				ITEMS_PER_PAGE);

		builderResponse.setQueryString(listTopNQuery);
		builderResponse.setQueryStringforCount(sqlQueryCount);

		logger.info("Query to fetch Count: [{}]", sqlQueryCount);
		logger.info("SQL Query Formed : [{}]", sqlQuery);
		return builderResponse;

	}

	/**
	 * 
	 * SQL Query Builder to build SQL for listing below Entities
	 * User,Invitation, Device and Credential. This Method is supports Filters
	 * of Type REST.
	 * 
	 * @param entity
	 * @param filters
	 * @return
	 * @throws Exception
	 */
	public QueryBuilderResponse sqlQueryBuilder(Schemas schema, String filters,
			QueryFilterId filterByIds, String sortBy, String sortOrder,
			int startIndex, int count) throws FilterParserException {

		scimAttrToSqlColumnMap = getScimAttrToSqlColumnMap(schema);

		QueryBuilderResponse builderResponse = new QueryBuilderResponse();
		String queryAttributes = getQueryAttributes();
		String queryTable = getQueryTable();
		List<Object> queryParams = new ArrayList<Object>();
		String queryFilters = getQueryFilterById(builderResponse, schema,
				filterByIds, queryParams);
		SearchFilter searchFilter;

		if (!(filters == null || filters.isEmpty())) {


			searchFilter = searchFilterParser.parseSearchFilterExpression(
					filters, schema);
			if (searchFilter.getWhereClause() == null
					|| searchFilter.getWhereClause().isEmpty()) {
				throw new FilterParserException(
						String.format(
								"Exception while parsing filter, provided filter string %s is invalid.",
								filters), HIDContants.INVALIDSYNTAX,
						Response.Status.BAD_REQUEST.getStatusCode());
			}
			queryFilters = searchFilter.getWhereClause().isEmpty() ? queryFilters
					: stringBuilder(queryFilters, " AND ",
							searchFilter.getWhereClause());
			for (Object o : searchFilter.getSearchParams()) {
				queryParams.add(o);
			}

		}

		builderResponse.setCountQueryParams(queryParams);

		// formatFilter(entity, filters, filterByIds,paramsList);

		sortBy = getSortingCriteria(sortBy, sortOrder);

		String sqlQuery = stringBuilder(SELECT, queryAttributes, QUERY_FROM,
				queryTable, QUERY_FILTER, queryFilters, sortBy);
		String sqlQueryCount = stringBuilder(SELECT, COUNT, QUERY_FROM,
				queryTable, QUERY_FILTER, queryFilters, sortBy);

		String listTopNQuery = paginate(builderResponse, sqlQuery, startIndex,
				count);

		builderResponse.setQueryString(listTopNQuery);
		builderResponse.setQueryStringforCount(sqlQueryCount);

		return builderResponse;

	}

	private Properties getScimAttrToSqlColumnMap(Schemas schema) {
		// TODO Auto-generated method stub

		if (schema == Schemas.PACSUSER) {
			scimAttrToSqlColumnMap = pacsUserAttrToSqlColumnMap;

		} else if (schema == Schemas.INVITATIONS) {
			scimAttrToSqlColumnMap = invitationAttrToSqlColumnMap;
		}

		else if (schema == Schemas.PART_NUMBER) {
			scimAttrToSqlColumnMap = partNoAttrToSqlColumnMap;
		}

		else if ((schema == Schemas.MOBILE_ID)
				|| (schema == Schemas.ACTIVE_MOBILEID)) {
			scimAttrToSqlColumnMap = mobileIdAttrToSqlColumnMap;
		}

		else if (schema == Schemas.DEVICES) {
			scimAttrToSqlColumnMap = deviceAttrToSqlColumnMap;
		}

		else if (schema == Schemas.DEVICE_MOBILEID) {
			scimAttrToSqlColumnMap = deviceMobileIdAttrToSqlColumnMap;
		}

		return scimAttrToSqlColumnMap;
	}

	private String getQueryTable() {
		// TODO Auto-generated method stub
		return scimAttrToSqlColumnMap.getProperty(HIDContants.TABLE).toString();
	}

	private String paginate(QueryBuilderResponse builderResponse,
			String sqlQuery, int startIndex, int count) {
		// TODO Auto-generated method stub

		startIndex = (startIndex == 0) ? START_INDEX : startIndex;
		int maxRow = (count <= ITEMS_PER_PAGE) ? count : ITEMS_PER_PAGE;
		logger.info("Setting Start Index to : [{}] ", startIndex);
		logger.info("Setting Max Row to : [{}] ", maxRow);
		maxRow = startIndex + maxRow - 1;

		String topNQuery = stringBuilder(
				"select *   from ( select a.*, rownum rnum from (", sqlQuery,
				") a where rownum <= ? ) where rnum >= ?");

		List<Object> queryParams = new ArrayList<Object>();

		for (Object o : builderResponse.getCountQueryParams()) {
			queryParams.add(o);
		}
		queryParams.add(maxRow);
		queryParams.add(startIndex);

		builderResponse.setQueryParams(queryParams);

		logger.info("SQL Query Formed - TOP 'N' [{}]", topNQuery);

		return topNQuery;
	}

	private String getDefaultSortOrder() {
		logger.info("Setting Default Sort Order");
		String sortBy = stringBuilder(SORTBY, scimAttrToSqlColumnMap
				.getProperty(HIDContants.LAST_MODIFIED_DT).toString(), " ",
				DESCENDING);

		logger.info("SortBy Formed:  [{}]", sortBy);
		return sortBy;
	}

	private String getQueryFilterById(QueryBuilderResponse builderResponse,
			Schemas schema, QueryFilterId filters, List<Object> queryParams) {

		String queryFilters = stringBuilder(
				scimAttrToSqlColumnMap.getProperty(HIDContants.COMPANY_ID),
				"= ?");

		queryParams.add(filters.getCompanyId());

		if (filters.getCompanyUserId() != null) {

			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.COMPANY_USER_ID), "= ?");

			queryParams.add(filters.getCompanyUserId());

		}

		if (filters.getInvitationId() != null) {

			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.INVITATION_ID), "= ?");

			queryParams.add(filters.getInvitationId());

		}

		if (filters.getPartNumberId() != null) {

			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.PARTNUMBER_ID), "= ?");

			queryParams.add(String.valueOf(filters.getPartNumberId()));

		}

		if (filters.getMobileId() != null) {

			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap.getProperty(HIDContants.MOBILE_ID),
					"= ?");

			queryParams.add(String.valueOf(filters.getMobileId()));

		}

		if (filters.getDeviceId() != null) {
			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.DEVICE_ENDPOINT_ID), "= ?");

			queryParams.add(String.valueOf(filters.getDeviceId()));
		}

		if (schema == Schemas.PACSUSER) {
			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.USER_STATUS_ID), " IN ( ",
					String.valueOf(HIDContants.USER_ACTIVE), " , ",
					String.valueOf(HIDContants.USER_DELETE_INITIATED), " )");

		}

		else if (schema == Schemas.ACTIVE_MOBILEID) {
			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.CREDENTIAL_STATUS_ID),
					"=", String.valueOf(HIDContants.MOBILEID_ACTIVE));

		} else if (schema == Schemas.DEVICES) {
			queryFilters = stringBuilder(queryFilters, LOGICAL_OPERATOR_AND,
					scimAttrToSqlColumnMap
							.getProperty(HIDContants.INVITATION_ID),
					"=", String.valueOf(HIDContants.INVITATION_ACKNOWLEDGED));

		}

		builderResponse.setCountQueryParams(queryParams);
		logger.info("Query Filter Formed with IDs: [{}]", queryFilters);
		return queryFilters;
	}

	private String getQueryAttributes() {

		return scimAttrToSqlColumnMap.getProperty(HIDContants.ATTRIBUTES)
				.toString();

	}

	private String getSortingCriteria(String sortBy, String sortOrder) {
		// TODO Auto-generated method stub

		String sortingCriteria = null;

		if (sortBy == null) {
			sortBy = "";
		}
		if (sortOrder == null) {
			sortOrder = "";
		}
		sortBy = sortBy.toLowerCase();
		sortOrder = sortOrder.toLowerCase();
		sortingCriteria = stringBuilder(SORTBY, scimAttrToSqlColumnMap
				.getProperty(sortBy, HIDContants.DEFAULT_SORT).toString(), " ",
				scimAttrToSqlColumnMap.getProperty(sortOrder, DESCENDING));

		logger.info("Sorting Criteria Formed: [{}]", sortingCriteria);

		return sortingCriteria;
	}

	private String stringBuilder(String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			if (message != null) {
				sb.append(message);
			}
		}
		return sb.toString();
	}

	public Properties getDbColumnMap() {
		return dbColumnMap;
	}

	public void setDbColumnMap(Properties dbColumnMap) {
		this.dbColumnMap = dbColumnMap;
	}

	public SearchFilterParser getSearchFilterParser() {
		return searchFilterParser;
	}

	public void setSearchFilterParser(SearchFilterParser searchFilterParser) {
		this.searchFilterParser = searchFilterParser;
	}

}
