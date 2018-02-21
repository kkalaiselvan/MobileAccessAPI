package com.hidglobal.managedservices.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.IssueCredentialException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.mapper.CredentialRowMapper;
import com.hidglobal.managedservices.proc.IssueCredential;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.QueryBuilder;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.PacsUserActions;
import com.hidglobal.managedservices.vo.QueryBuilderResponse;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.UserMobileAction;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchMobileIdResponse;
import com.hidglobal.managedservices.vo.SearchRequest;

/**
 * 
 * This class mainly interacts with database through stored procedure and
 * Queries .
 * 
 */
@Repository
public class MobileIdsDAO extends AbstractDAO {
	@Autowired
	QueryBuilder queryBuilder;
	@Autowired
	private UtilsDAO utilsDAO;
	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<MobileId> getMobileIdsResultSet(String sqlQuery,
			List<Object> paramsList) {

		List<MobileId> mobileIdsRowObject = new ArrayList<MobileId>();
		mobileIdsRowObject = (List<MobileId>) jdbcTemplateObject.query(
				sqlQuery, paramsList.toArray(), new CredentialRowMapper());
		return mobileIdsRowObject;

	}

	/**
	 * This method Interacts with Database Stored procedure and return the
	 * available MobileId for the requested User Action
	 * 
	 * @param pacsUserAction
	 * @param companyId
	 * @return This returns the Available MobileId for requested User Action
	 * @throws InvitationException
	 * @throws SQLException
	 */
	public Long checkAvailableCredential(PacsUserActions pacsUserAction,
			String companyId) throws InvitationException, SQLException {
		Long compId = new Long(companyId);
		Object[] paramList;
		if ((pacsUserAction != null)
				&& !(pacsUserAction.getPartNumber() == null)) {
			paramList = new Object[] { pacsUserAction.getPartNumber().trim(),
					pacsUserAction.getMobileId(), compId,
					pacsUserAction.getAssignMobileId().toUpperCase().trim() };
		} else {
			paramList = new Object[] { null, pacsUserAction.getMobileId(),
					compId,
					pacsUserAction.getAssignMobileId().toUpperCase().trim() };
		}

		int[] argTypes = new int[] { Types.VARCHAR, Types.NUMERIC,
				Types.NUMERIC, Types.VARCHAR };

		Long credentialValue = jdbcTemplateObject.queryForObject(
				HIDContants.CHECK_AVAILABLE_CREDENTIAL_SQL, paramList,
				argTypes, Long.class);
		logger.info("Available credential for the Request [{}]",
				credentialValue);
		if (credentialValue == -5) {
			throw new InvitationException("Invalid Request-", null, "AC-5",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (credentialValue == -1) {
			throw new InvitationException("Invalid Request-", null, "AC-1",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (credentialValue == -2) {
			throw new InvitationException(companyId, null, "AC-2",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (credentialValue == -3) {
			throw new InvitationException(pacsUserAction.getMobileId()
					.toString(), null, "AC-3", HIDContants.INVALIDVALUE,
					HIDContants.BADREQUEST);
		}
		if (credentialValue == -4) {
			throw new InvitationException(null, null, "AC-4",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		return credentialValue;

	}

	/**
	 * This method interacts with database through Query and fetch MobileId
	 * Details for a requested MobileId.
	 * 
	 * @param filterByIds
	 * @return
	 * @throws DBException
	 */
	public MobileId fetchMobileIdDetails(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.MOBILE_ID,
				filterByIds);

		List<MobileId> mobileIdsRowObject = getMobileIdsResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		MobileId mobileId = new MobileId();

		if (mobileIdsRowObject.size() == 1) {
			mobileId = mobileIdsRowObject.get(0);
		}

		else {
			logger.error("Get MobileId Details Failed for MobileId [{}] ",
					filterByIds.getMobileId());

			throw new DBException(String.valueOf(filterByIds.getMobileId()),
					String.valueOf(HIDContants.INVALID_MOBILEID),
					HIDContants.NOTARGET);
		}
		return mobileId;
	}

	/**
	 * This method interacts with database through Query and fetch all the
	 * MobileId for a requested Device and partNumber.
	 * 
	 * @param filterByIds
	 * @param operationName
	 */
	public SearchMobileIdResponse listMobileIds(QueryFilterId filterByIds,
			String operationName) {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse = null;

		if (operationName.equalsIgnoreCase(HIDContants.DEVICE_ENTITY)) {
			builderResponse = queryBuilder.sqlQueryBuilder(
					Schemas.DEVICE_MOBILEID, filterByIds);
		} else {
			builderResponse = queryBuilder.sqlQueryBuilder(
					Schemas.ACTIVE_MOBILEID, filterByIds);
		}
		int count = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.info("Total Mobile Ids Found for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), count);

		List<MobileId> mobileIdsRowObject = getMobileIdsResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());

		List<MobileId> mobileIds = new ArrayList<MobileId>();

		for (MobileId mId : mobileIdsRowObject) {
			MobileId mobileId = new MobileId();
			mobileId.setMeta(mId.getMeta());
			mobileId.getMeta().setLocation(
					MessageFormat.format(mobileId.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			mobileIds.add(mobileId);
		}
		SearchMobileIdResponse mobileIdsSearchResult = new SearchMobileIdResponse();

		mobileIdsSearchResult.setTotalResults(count);
		mobileIdsSearchResult.setItemsPerPage(mobileIdsRowObject.size());
		mobileIdsSearchResult.setStartIndex(QueryBuilder.START_INDEX);
		logger.info("Items Per Page: [{}]",
				mobileIdsSearchResult.getItemsPerPage());
		logger.info("Start Index: [{}]", mobileIdsSearchResult.getStartIndex());
		mobileIdsSearchResult.setMobileIds(mobileIds);
		return mobileIdsSearchResult;
	}

	/**
	 * This method interact with database through query and fetch all the
	 * details of the requested search request
	 * 
	 * @param searchRequest
	 * @param filterByIds
	 * @throws Exception
	 */
	public SearchMobileIdResponse searchMobileIds(SearchRequest searchRequest,
			QueryFilterId filterByIds) throws Exception {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		int startIndex = (searchRequest.getStartIndex() < 1) ? QueryBuilder.START_INDEX
				: searchRequest.getStartIndex();
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.MOBILE_ID,
				searchRequest.getFilterList(), filterByIds,
				searchRequest.getSortBy(), searchRequest.getSortingOrder(),
				startIndex, searchRequest.getCount());

		List<MobileId> mobileIdsRowObject = getMobileIdsResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		int countRows = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.info("Total MobileIds found for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), countRows);
		SearchMobileIdResponse mobileIdsSearchResult = new SearchMobileIdResponse();
		List<MobileId> mobileIds = new ArrayList<MobileId>();

		for (MobileId mId : mobileIdsRowObject) {
			MobileId mobileId = new MobileId();

			if (searchRequest.getAttributes() != null) {
				for (String attribute : searchRequest.getAttributes()) {

					if (HIDContants.ID.equalsIgnoreCase(attribute)) {
						mobileId.setId(mId.getId());
					} else if (HIDContants.PART_NUMBER
							.equalsIgnoreCase(attribute)) {
						mobileId.setPartNumber(mId.getPartNumber());
					} else if (HIDContants.FRIENDLY_NAME
							.equalsIgnoreCase(attribute)) {
						mobileId.setPartnumberFriendlyName(mId
								.getPartnumberFriendlyName());
					} else if (HIDContants.CARD_NUMBER
							.equalsIgnoreCase(attribute)) {
						mobileId.setCardNumber(mId.getCardNumber());
					} else if (HIDContants.REFERENCE_NUMBER
							.equalsIgnoreCase(attribute)) {
						mobileId.setReferenceNumber(mId.getReferenceNumber());
					} else if (HIDContants.CREDENTIAL_STATUS
							.equalsIgnoreCase(attribute)) {
						mobileId.setStatus(mId.getStatus());
					} else if (HIDContants.CREDENTIAL_TYPE
							.equalsIgnoreCase(attribute)) {
						mobileId.setCredentialIdType(mId.getCredentialIdType());
					}
				}
			}
			mobileId.setMeta(mId.getMeta());
			mobileId.getMeta().setLocation(
					MessageFormat.format(mobileId.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			mobileIds.add(mobileId);
		}

		mobileIdsSearchResult.setTotalResults(countRows);
		mobileIdsSearchResult.setItemsPerPage(mobileIds.size());
		mobileIdsSearchResult.setStartIndex(startIndex);
		logger.info("Items Per Page: [{}]",
				mobileIdsSearchResult.getItemsPerPage());
		logger.info("Start Index: [{}]",
				mobileIdsSearchResult.getItemsPerPage());
		mobileIdsSearchResult.setMobileIds(mobileIds);
		return mobileIdsSearchResult;
	}

	/**
	 * This method interacts with database through query and fetch the
	 * EndpointId by passing MobileId.
	 * 
	 * @param mobileId
	 * @return This holds the EndpointId Value
	 * @throws MAValidationException
	 */
	public Long fetchEndpointId(Long mobileId) throws MAValidationException {
		Object[] paramList = new Object[] { mobileId };
		int[] argTypes = new int[] { Types.NUMERIC };
		Long endpointId;
		try {
			endpointId = jdbcTemplateObject.queryForObject(
					HIDContants.GET_ENDPOINT_SQL, paramList, argTypes,
					Long.class);
			logger.info("Endpoint Id for the given mobileId -[{}]", endpointId);

		} catch (EmptyResultDataAccessException e) {
			throw new MAValidationException(mobileId.toString(), null,
					"invalid.mobileId", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}
		return endpointId;
	}

	/**
	 * This method interacts with database through query and fetch the status of
	 * the requested MobileId.
	 * 
	 * @param companyId
	 * @param mobileId
	 * @return This holds the status of the MobileId
	 * @throws MobileIdException
	 */
	public Long checkCredentialStatus(Long companyId, Long mobileId)
			throws MobileIdException {
		Object[] paramList = new Object[] { companyId, mobileId };
		int[] argTypes = new int[] { Types.NUMERIC, Types.NUMERIC };

		Long status;
		try {
			status = jdbcTemplateObject.queryForObject(
					HIDContants.CHECK_CREDENTIAL_STATUS, paramList, argTypes,
					Long.class);
			logger.info("status of the given mobileId -[{}]", status);

			if (status == 757 || status == 5) {
				
					throw new MobileIdException(mobileId.toString(), null,
							"invalid.mobileId", HIDContants.MUTABILITY,
							HIDContants.PRECONDITIONFAILED);
				}
			

			if (status == 755 || status == 754) {
				
					throw new MobileIdException(mobileId.toString(), null,
							"CS-755", HIDContants.MUTABILITY,
							HIDContants.PRECONDITIONFAILED);
				
			}

		} catch (EmptyResultDataAccessException e) {
			throw new MobileIdException(mobileId.toString(), null, "CS-752",
					HIDContants.NOTARGET, HIDContants.REQUESTNOTFOUND);
		}

		return status;
	}

	/**
	 * This method interacts with database through query and change the status
	 * of the requested MobileId
	 * 
	 * @param mobileId
	 */
	public void initateRevokeMobileId(Long mobileId) {
		Object[] paramList = new Object[] { HIDContants.REVOKE_INITIATED,
				mobileId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		jdbcTemplateObject.update(HIDContants.INITIATE_MOBILEID_SQL, paramList,
				argTypes);
	}

	/**
	 * This method interacts with database through Stored Procedure and validate
	 * the requested DeviceId , MobileId prerequisites
	 * 
	 * @param companyId
	 * @param deviceId
	 * @param requestor
	 * @param userMobileAction
	 * @throws Exception
	 */
	public MobileId issueMobileId(Long companyId, Long deviceId,
			String requestor, UserMobileAction userMobileAction)
			throws Exception {

		Map<String, Object> inParams = new HashMap<String, Object>();
		Map<String, Object> outParams = new HashMap<String, Object>();
		if ((userMobileAction != null)
				&& (userMobileAction.getUserActions().getMobileId() != null)) {
			inParams.put("pio_credential_id", userMobileAction.getUserActions()
					.getMobileId());
		} else {
			inParams.put("pio_credential_id", null);
		}
		inParams.put("pi_device_id", deviceId);
		if ((userMobileAction != null)
				&& (userMobileAction.getUserActions().getPartNumber() != null)) {
			inParams.put("pio_part_number", userMobileAction.getUserActions()
					.getPartNumber());
		} else {
			inParams.put("pio_part_number", null);
		}
		inParams.put("pi_company_id", companyId);
		inParams.put("pi_requestor", requestor);
		IssueCredential issueCredential = new IssueCredential(this.dataSource);
		outParams = issueCredential.execute(inParams);
		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();
		if (po_retval == -20014) {
			throw new IssueCredentialException(null, null, "-20014",
					HIDContants.MUTABILITY, HIDContants.PRECONDITIONFAILED);
		}
		if (po_retval == -20091) {
			throw new IssueCredentialException(inParams.get("pi_device_id")
					.toString(), null, Integer.toString(po_retval),
					HIDContants.MUTABILITY, HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20092) {
			throw new IssueCredentialException(inParams.get("pi_device_id")
					.toString(), null, Integer.toString(po_retval),
					HIDContants.NOTARGET, HIDContants.REQUESTNOTFOUND);

		}
		if (po_retval == -20055) {
			throw new IssueCredentialException(inParams.get("pi_company_id")
					.toString(), null, Integer.toString(po_retval),
					HIDContants.NOTARGET, HIDContants.REQUESTNOTFOUND);

		}
		if (po_retval == -20090) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20089) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20088) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20051) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20086) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20085) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}
		if (po_retval == -20084) {
			throw new IssueCredentialException(null, null,
					Integer.toString(po_retval), HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);

		}

		if (po_retval == 0 && outParams.get("pio_credential_id") != null) {
			MobileId mobileId = new MobileId();
			Long credentialId = new Long(outParams.get("pio_credential_id")
					.toString());
			System.out.println("!!!!"
					+ outParams.get("pio_credential_id").toString() + "!!!"
					+ ((BigDecimal) outParams.get("po_retval")).intValue());
			mobileId.setId(credentialId);
			if (outParams.get("po_card_number") != null) {
				mobileId.setCardNumber(outParams.get("po_card_number")
						.toString());
			}
			if (outParams.get("pio_part_number") != null) {
				mobileId.setPartNumber(outParams.get("pio_part_number")
						.toString());
			}
			if (outParams.get("po_freindly_name") != null) {
				mobileId.setPartnumberFriendlyName(outParams.get(
						"po_freindly_name").toString());
			}
			if (outParams.get("po_reference_number") != null) {
				mobileId.setReferenceNumber(outParams
						.get("po_reference_number").toString());
			} else {
				mobileId.setReferenceNumber("");
			}
			if (outParams.get("po_credential_status") != null) {
				mobileId.setStatus(outParams.get("po_credential_status")
						.toString());
			}
			if (outParams.get("po_credential_type") != null) {
				mobileId.setCredentialIdType(outParams
						.get("po_credential_type").toString());
			}
			Meta meta = new Meta();
			meta.setResourceType(HIDContants.MOBILEIDRESOURCETYPE);
			meta.setLocation(resourceLocation
					.getProperty(HIDContants.MOBILEIDLOCATION).concat("/")
					.concat(mobileId.getId().toString()));
			meta.setLocation(MessageFormat.format(meta.getLocation(),
					Long.toString(companyId)));
			if (outParams.get("po_last_modified_dt") != null) {
				meta.setLastModified(outParams.get("po_last_modified_dt")
						.toString());
			}
			mobileId.setMeta(meta);
			return mobileId;
		} else {
			logger.debug(outParams.get("po_retmsg")
					.toString());
			throw new MobileAccessException(outParams.get("po_retmsg")
					.toString());
		}

	}

	/**
	 * This method interacts with database through query and change the status
	 * of the requested MobileId.
	 * 
	 * @param mobileId
	 */
	public void initiateIssueMobileId(Long mobileId) {
		Object[] paramList = new Object[] { HIDContants.ISSUE_INITIATED,
				mobileId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		jdbcTemplateObject.update(HIDContants.INITIATE_MOBILEID_SQL, paramList,
				argTypes);

	}
}
