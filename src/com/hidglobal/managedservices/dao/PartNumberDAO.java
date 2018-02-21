package com.hidglobal.managedservices.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.mapper.PartNumberRowMapper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.QueryBuilder;
import com.hidglobal.managedservices.vo.PartNumber;
import com.hidglobal.managedservices.vo.QueryBuilderResponse;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchPartNumberResponse;
import com.hidglobal.managedservices.vo.SearchRequest;

/**
 * This class mainly interacts with database for PartNumber related information
 * through stored procedure and Queries .
 * 
 * 
 */
@Repository
public class PartNumberDAO extends AbstractDAO {

	@Autowired
	QueryBuilder queryBuilder;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UtilsDAO utilsDAO;

	/**
	 * This method interacts with database through query and fetch the
	 * PartNumber details for the given request.
	 * 
	 * @param filters
	 * @return
	 * @throws DBException
	 */
	public PartNumber fetchPartNumberDetails(QueryFilterId filters)
			throws DBException {
		// TODO Auto-generated method stub

		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.PART_NUMBER,
				filters);

		List<PartNumber> partNumberRowObject = getPartNumberResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		PartNumber partNumber = new PartNumber();

		if (partNumberRowObject.size() == 1) {
			partNumber = partNumberRowObject.get(0);
		}

		else {
			logger.error("Get Part Number Details Failed for User ID [{}]",
					filters.getInvitationId());

			throw new DBException(String.valueOf(filters.getPartNumberId()),
					String.valueOf(HIDContants.INVALID_PARTNUMBER),
					HIDContants.NOTARGET);
		}
		return partNumber;
	}

	/**
	 * This method interacts with database through query and fetch the
	 * PartNumber details for the given request.
	 * 
	 * @param sqlQuery
	 * @param paramsList
	 * @return
	 */
	private List<PartNumber> getPartNumberResultSet(String sqlQuery,
			List<Object> paramsList) {

		List<PartNumber> partNumberRowObject = new ArrayList<PartNumber>();
		partNumberRowObject = (List<PartNumber>) jdbcTemplateObject.query(
				sqlQuery, paramsList.toArray(), new PartNumberRowMapper());
		return partNumberRowObject;

	}

	/**
	 * This method interacts with database through query and List the PartNumber
	 * details for the given request.
	 * 
	 * @param filters
	 * @return
	 */
	public SearchPartNumberResponse listPartNumbers(QueryFilterId filters) {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.PART_NUMBER,
				filters);

		int count = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.info("Total Part Numbers Found for Company ID [{}] is [{}]",
				filters.getCompanyId(), count);

		List<PartNumber> partNumberRowObject = getPartNumberResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());

		List<PartNumber> partNumbers = new ArrayList<PartNumber>();

		for (PartNumber p : partNumberRowObject) {
			PartNumber partNumber = new PartNumber();
			partNumber.setMeta(p.getMeta());
			partNumber.getMeta().setLocation(
					MessageFormat.format(partNumber.getMeta().getLocation(),
							Long.toString(filters.getCompanyId())));
			partNumbers.add(partNumber);
		}
		SearchPartNumberResponse partNumberResponse = new SearchPartNumberResponse();

		partNumberResponse.setTotalResults(count);
		partNumberResponse.setItemsPerPage(partNumberRowObject.size());
		partNumberResponse.setStartIndex(QueryBuilder.START_INDEX);
		logger.info("Items Per Page: [{}]",
				partNumberResponse.getItemsPerPage());
		logger.info("Start Index: [{}]", partNumberResponse.getItemsPerPage());
		partNumberResponse.setPartNumbers(partNumbers);
		return partNumberResponse;
	}

	/**
	 * This method interacts with database through query and get the PartNumber
	 * details for the given request.
	 * 
	 * @param searchRequest
	 * @param filterByIds
	 * @return
	 * @throws Exception
	 */
	public SearchPartNumberResponse searchPartNumberDetails(
			SearchRequest searchRequest, QueryFilterId filterByIds)
			throws Exception {
		QueryBuilderResponse builderResponse;
		int startIndex = (searchRequest.getStartIndex() < 1) ? QueryBuilder.START_INDEX
				: searchRequest.getStartIndex();
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.PART_NUMBER,
				searchRequest.getFilterList(), filterByIds,
				searchRequest.getSortBy(), searchRequest.getSortingOrder(),
				startIndex, searchRequest.getCount());

		List<PartNumber> partNoRowObject = getPartNumberResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		int countRows = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.info("Total Part Numbers found for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), countRows);
		SearchPartNumberResponse partNoSearchResult = new SearchPartNumberResponse();
		List<PartNumber> partNumbers = new ArrayList<PartNumber>();

		for (PartNumber p : partNoRowObject) {
			PartNumber partNumber = new PartNumber();

			if (searchRequest.getAttributes() != null) {
				for (String attribute : searchRequest.getAttributes()) {

					if (HIDContants.ID.equalsIgnoreCase(attribute)) {
						partNumber.setId(p.getId());
					}

					else if (HIDContants.PART_NUMBER
							.equalsIgnoreCase(attribute)) {
						partNumber.setPartNumber(p.getPartNumber());
					} else if (HIDContants.PARTNUMBER_FRIENDLY_NAME
							.equalsIgnoreCase(attribute)) {
						partNumber.setFriendlyName(p.getFriendlyName());
					} else if (HIDContants.PARTNUMBER_DESCRIPTION
							.equalsIgnoreCase(attribute)) {
						partNumber.setDescription(p.getDescription());
					} else if (HIDContants.AVAILABLE_QTY
							.equalsIgnoreCase(attribute)) {
						partNumber.setAvailableQty(p.getAvailableQty());
					}
				}
			}
			partNumber.setMeta(p.getMeta());
			partNumber.getMeta().setLocation(
					MessageFormat.format(partNumber.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			partNumbers.add(partNumber);
		}

		partNoSearchResult.setTotalResults(countRows);
		partNoSearchResult.setItemsPerPage(partNumbers.size());
		partNoSearchResult.setStartIndex(startIndex);
		logger.info("Items Per Page: [{}]",
				partNoSearchResult.getItemsPerPage());
		logger.info("Start Index: [{}]", partNoSearchResult.getItemsPerPage());
		partNoSearchResult.setPartNumbers(partNumbers);
		return partNoSearchResult;
	}

}
