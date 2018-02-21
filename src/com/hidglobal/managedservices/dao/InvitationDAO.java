package com.hidglobal.managedservices.dao;

import java.text.MessageFormat;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.mapper.CheckInvitationIdRowMapper;
import com.hidglobal.managedservices.mapper.CheckInvitationStatusRowMapper;
import com.hidglobal.managedservices.mapper.PacsInvitationRowMapper;
import com.hidglobal.managedservices.proc.CreateInvitation;
import com.hidglobal.managedservices.proc.SendInvitationEmail;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.QueryBuilder;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.PacsUserActions;
import com.hidglobal.managedservices.vo.QueryBuilderResponse;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchInvitationResponse;
import com.hidglobal.managedservices.vo.UserInvitations;

/**
 * 
 * 
 * This class mainly interacts with database for Invitation related information
 * through stored procedure and Queries .
 * 
 */
@Repository
public class InvitationDAO extends AbstractDAO {
	private static final Logger logger = LoggerFactory
			.getLogger(InvitationDAO.class);
	@Autowired
	QueryBuilder queryBuilder;
	@Autowired
	UtilsDAO utilsDAO;

	/**
	 * This method interacts with database through Stored Procedure and it will
	 * make a entry in the database for the new Invitation.
	 * 
	 * @param compnayUserId
	 * @param userInvitation
	 * @param pacsUserAction
	 * @param credentialValue
	 * @param expiryTimeinMinutes
	 * @param companyId
	 * @param requestor
	 * @return
	 * @throws DBException
	 * @throws MobileAccessException
	 * @throws MobileIdException
	 * @throws InvitationException
	 */
	public UserInvitations createInvitation(Long compnayUserId,
			UserInvitations userInvitation, PacsUserActions pacsUserAction,
			 Integer expiryTimeinMinutes,
			String companyId, String requestor) throws DBException,
			MobileAccessException, MobileIdException, InvitationException {
		Map<String, Object> inParams = new HashMap<String, Object>();
		Map<String, Object> outParams = new HashMap<String, Object>();
		Long compId = new Long(companyId);
		inParams.put("pi_company_id", compId);
		inParams.put("pi_company_user_id", compnayUserId);
		inParams.put("pi_aamk_invitation_id", userInvitation.getInvitations()
				.get(0).getAamkInvitationId());
		inParams.put("pi_aamk_invitation_code", userInvitation.getInvitations()
				.get(0).getInvitationCode());
		if ((pacsUserAction != null)
				&& (pacsUserAction.getPartNumber() != null)
				&& (!pacsUserAction.getPartNumber().trim().isEmpty())) {
			inParams.put("pio_part_number", pacsUserAction.getPartNumber()
					.trim());

		} else {
			inParams.put("pio_part_number", null);
		}
		if ((pacsUserAction != null) && (pacsUserAction.getMobileId() != null)
				&& (!pacsUserAction.getMobileId().toString().trim().isEmpty())) {
			inParams.put("pio_credential_id", pacsUserAction.getMobileId()
					.toString().trim());
		} else {
			inParams.put("pio_credential_id", null);
		}
		if ((pacsUserAction != null)
				&& (pacsUserAction.getAssignMobileId() != null)) {
			inParams.put("pi_auto_assign", pacsUserAction.getAssignMobileId()
					.toUpperCase());
		} else {
			inParams.put("pi_auto_assign", null);
		}
		inParams.put("pio_created_ts", userInvitation.getInvitations().get(0)
				.getCreatedDate());
		inParams.put("pi_expiration_time_in_min", expiryTimeinMinutes);
		inParams.put("pi_status_id", HIDContants.INVITATION_PENDING_STATUS);
		inParams.put("pio_expiration_ts", userInvitation.getInvitations()
				.get(0).getExpirationDate());
		inParams.put("pi_requestor", requestor);
		CreateInvitation pacsCreateInvitation = new CreateInvitation(
				this.dataSource);
		logger.debug("Create Invitation Input Params: [{}] ", inParams);
		outParams = pacsCreateInvitation.execute(inParams);
		logger.debug("Create InvitationOutput Params: [{}] ", outParams);
		String po_retmsg = (String) outParams.get("po_retmsg");
		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();

		if (po_retval == 0) {
			logger.debug("create Invitation successful [{}]" + outParams);
			if (outParams.get("po_card_number") != null) {
				userInvitation
						.getMobileId()
						.get(0)
						.setCardNumber(
								outParams.get("po_card_number").toString());
			}
			if (outParams.get("po_last_modified_dt") != null) {
				userInvitation.getMeta().setLastModified(
						outParams.get("po_last_modified_dt").toString());
			}
			if (outParams.get("po_credential_type") != null) {
				userInvitation
						.getMobileId()
						.get(0)
						.setCredentialIdType(
								outParams.get("po_credential_type").toString());
			}
			if (outParams.get("pio_part_number") != null) {
				userInvitation
						.getMobileId()
						.get(0)
						.setPartNumber(
								outParams.get("pio_part_number").toString());
			}
			if (outParams.get("po_credential_status") != null) {
				userInvitation
						.getMobileId()
						.get(0)
						.setStatus(
								outParams.get("po_credential_status")
										.toString());
			}
			if (outParams.get("po_freindly_name") != null) {
				userInvitation
						.getMobileId()
						.get(0)
						.setPartnumberFriendlyName(
								outParams.get("po_freindly_name").toString());
			}

			if (outParams.get("po_reference_number") != null) {
				userInvitation
						.getMobileId()
						.get(0)
						.setReferenceNumber(
								outParams.get("po_reference_number").toString());
			}
			if (outParams.get("pio_credential_id") != null) {
				Long credenId = new Long(outParams.get("pio_credential_id")
						.toString());
				userInvitation.getMobileId().get(0).setId(credenId);
			}
			if (outParams.get("pio_created_ts") != null) {
				userInvitation
						.getInvitations()
						.get(0)
						.setCreatedDate(
								outParams.get("pio_created_ts").toString());
			}
			if (outParams.get("pio_expiration_ts") != null) {
				userInvitation
						.getInvitations()
						.get(0)
						.setExpirationDate(
								outParams.get("pio_expiration_ts").toString());
			}
			return userInvitation;

		} else if (po_retval == -20015) {
			throw new InvitationException(companyId, null, "AC-2",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		} else if (po_retval == HIDContants.INVALID_AUTOASSIGN_CREDENTIAL) {
			logger.error("create Invitation Failed -[{}]", po_retmsg);

			throw new MobileIdException(null, null,
					"-20014", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}

		else if (po_retval == HIDContants.INVALID_USER) {
			logger.error("create Invitation Failed -[{}]", po_retmsg);

			throw new DBException(String.valueOf(compnayUserId),
					Integer.toString(po_retval), HIDContants.NOTARGET);
		} else {
			logger.error(
					"Create Invitation Failed. Mobile Access Databse Exception: Error Code: [{}], Error Details: [{}]",
					po_retval, po_retmsg);
			throw new MobileAccessException(po_retmsg);
		}

	}

	/**
	 * This method interacts with database through query and fetch the
	 * Invitation details for the given request.
	 * 
	 * @param filters
	 * @throws DBException
	 */
	public Invitation fetchInvitationDetails(QueryFilterId filters)
			throws DBException {

		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.INVITATIONS,
				filters);

		List<Invitation> invitationRowObject = getInvitationResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		Invitation invitation = new Invitation();

		if (invitationRowObject.size() == 1) {
			invitation = invitationRowObject.get(0);
		}

		else {
			logger.error(
					"Get Invitation Details Failed for Invitation ID [{}]",
					filters.getInvitationId());

			throw new DBException(String.valueOf(filters.getInvitationId()),
					String.valueOf(HIDContants.INVALID_INVITATION),
					HIDContants.NOTARGET);
		}
		return invitation;
	}

	/**
	 * This method interacts with database through query and get the invitation
	 * details for the given request
	 * 
	 * @param filters
	 * 
	 */
	public SearchInvitationResponse listInvitations(QueryFilterId filters) {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.INVITATIONS,
				filters);

		int count = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.debug("Total Invitations for Company ID [{}] is [{}]",
				filters.getCompanyId(), count);
		List<Invitation> invitationRowObject = getInvitationResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());

		SearchInvitationResponse invitationResponse = new SearchInvitationResponse();
		List<Invitation> invitations = new ArrayList<Invitation>();

		for (Invitation i : invitationRowObject) {
			Invitation invitation = new Invitation();
			invitation.setMeta(i.getMeta());
			invitation.getMeta().setLocation(
					MessageFormat.format(invitation.getMeta().getLocation(),
							Long.toString(filters.getCompanyId())));
			invitations.add(invitation);
		}
		invitationResponse.setTotalResults(count);
		invitationResponse.setItemsPerPage(invitationRowObject.size());
		invitationResponse.setStartIndex(QueryBuilder.START_INDEX);
		logger.debug("Items Per Page: [{}]",
				invitationResponse.getItemsPerPage());
		logger.debug("Start Index: [{}]", invitationResponse.getItemsPerPage());
		invitationResponse.setInvitations(invitations);
		return invitationResponse;
	}

	/**
	 * This method interacts with database through query and get the Invitation
	 * details as per request query.
	 * 
	 * @param queryAttributes
	 * @param queryFilters
	 * @param filterByIds
	 * @param sortBy
	 * @param sortingOrder
	 * @param startIndex
	 * @param maxCount
	 * @return
	 * @throws Exception
	 */
	public SearchInvitationResponse searchInvitation(
			List<String> queryAttributes, String queryFilters,
			QueryFilterId filterByIds, String sortBy, String sortingOrder,
			int startIndex, int maxCount) throws Exception {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		startIndex = (startIndex < 1) ? QueryBuilder.START_INDEX : startIndex;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.INVITATIONS,
				queryFilters, filterByIds, sortBy, sortingOrder, startIndex,
				maxCount);

		List<Invitation> invitationRowObject = getInvitationResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		int countRows = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.debug("Total Invitations for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), countRows);
		SearchInvitationResponse invitationSearchResult = new SearchInvitationResponse();
		List<Invitation> invitations = new ArrayList<Invitation>();

		for (Invitation invitation : invitationRowObject) {
			Invitation pacsInvitation = new Invitation();

			if (queryAttributes != null) {
				for (String attribute : queryAttributes) {

					if (HIDContants.ID.equalsIgnoreCase(attribute)) {
						pacsInvitation.setAamkInvitationId(invitation
								.getAamkInvitationId());
					}

					else if (HIDContants.AAMK_INVITATION_CODE
							.equalsIgnoreCase(attribute)) {
						pacsInvitation.setInvitationCode(invitation
								.getInvitationCode());
					} /*
					 * else if (HIDContants.CREATED_TS
					 * .equalsIgnoreCase(attribute)) {
					 * pacsInvitation.setCreatedDate(invitation
					 * .getCreatedDate()); } else if (HIDContants.EXPIRATION_TS
					 * .equalsIgnoreCase(attribute)) {
					 * pacsInvitation.setExpirationDate(invitation
					 * .getExpirationDate()); }
					 */else if (HIDContants.STATUS.equalsIgnoreCase(attribute)) {
						pacsInvitation.setStatus((invitation.getStatus()));
					}
				}
			}
			pacsInvitation.setMeta(invitation.getMeta());
			invitation.getMeta().setLocation(
					MessageFormat.format(invitation.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			invitations.add(pacsInvitation);
		}

		invitationSearchResult.setTotalResults(countRows);
		invitationSearchResult.setItemsPerPage(invitations.size());
		invitationSearchResult.setStartIndex(startIndex);
		logger.debug("Items Per Page: [{}]",
				invitationSearchResult.getItemsPerPage());
		logger.debug("Start Index: [{}]",
				invitationSearchResult.getItemsPerPage());
		invitationSearchResult.setInvitations(invitations);
		return invitationSearchResult;
	}

	private List<Invitation> getInvitationResultSet(String sqlQuery,
			List<Object> paramsList) {
		List<Invitation> invitationRowObject = new ArrayList<Invitation>();
		invitationRowObject = (List<Invitation>) jdbcTemplateObject.query(
				sqlQuery, paramsList.toArray(), new PacsInvitationRowMapper());
		return invitationRowObject;
	}

	/**
	 * This method interacts with database through query and get the status of
	 * the Invitation
	 * 
	 * @param invitationId
	 * @param companyId
	 * @return
	 * @throws InvitationException
	 * @throws MAValidationException
	 */
	public Long checkInvitationStatus(Long invitationId, Long companyId)
			throws InvitationException, MAValidationException {

		Object[] paramObject = new Object[] { companyId, invitationId };
		int[] argType = new int[] { Types.INTEGER, Types.INTEGER };
		List<Long> status = jdbcTemplateObject.query(
				HIDContants.INVITATION_STATUS_SQL, paramObject, argType,
				new CheckInvitationStatusRowMapper());

		logger.debug("Invitation status of the request [{}]", status);

		if (status.isEmpty()) {
			throw new InvitationException(invitationId.toString(), null,
					"invalid.invitation", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}

		if (status.get(0) == HIDContants.INVITATION_CANCELLED) {

			throw new InvitationException(invitationId.toString(), null,
					"invalid.invitation.cancelled", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}
		if (status.get(0) == HIDContants.INVITATION_ACKNOWLEDGED) {

			throw new InvitationException(invitationId.toString(), null,
					"invalid.invitation.acknowledged", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}
		if (status.get(0) == HIDContants.INVITATION_EXPIRED) {

			throw new InvitationException(invitationId.toString(), null,
					"invalid.invitation.expired", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}

		if (status.get(0) != HIDContants.INVITATION_PENDING_STATUS) {

			throw new InvitationException("request", null,
					"invalid.invitation.status", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}
		return status.get(0);

	}

	/**
	 * This method interacts with database through query and get the status of
	 * the invitation for the given InvitationId .
	 * 
	 * @param userId
	 * @param invitationId
	 * @param companyId
	 * @return
	 * @throws InvitationException
	 * @throws InvitationException
	 */
	public Long checkInvitationStatusForUser(Long userId, Long invitationId,
			Long companyId) throws InvitationException, InvitationException {
		Object[] paramList = new Object[] { userId, companyId,
				HIDContants.USER_ACTIVE, invitationId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.INTEGER };

		List<Long> invitation_Staus_Id = jdbcTemplateObject.query(
				HIDContants.CHECK_INVITATION_SQL, paramList, argTypes,
				new CheckInvitationIdRowMapper());
		if (invitation_Staus_Id.isEmpty()) {
			throw new InvitationException("request", null,
					"invalid.invitation.user", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}
		if (invitation_Staus_Id.get(0) == HIDContants.INVITATION_CANCELLED) {

			throw new InvitationException(invitationId.toString(), null,
					"invalid.invitation.cancelled", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}

		if (invitation_Staus_Id.get(0) != HIDContants.INVITATION_PENDING_STATUS) {

			throw new InvitationException(invitationId.toString(), null,
					"invalid.invitation.status", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}

		return invitation_Staus_Id.get(0);

	}

	/*
	 * public Long checkAvailableCredential(Long companyUserId, PacsUserActions
	 * pacsUserAction, String companyId) throws InvitationException,
	 * SQLException { Long compId = new Long(companyId); Object[] paramList;
	 * if(!(pacsUserAction.getPartNumber()==null)) { paramList = new Object[] {
	 * pacsUserAction.getPartNumber().trim(), pacsUserAction.getMobileID(),
	 * compId, pacsUserAction.getAssignMobileId().toUpperCase().trim() }; }else{
	 * paramList = new Object[] { null, pacsUserAction.getMobileID(), compId,
	 * pacsUserAction.getAssignMobileId().toUpperCase().trim() }; }
	 * 
	 * int[] argTypes = new int[] { Types.VARCHAR, Types.NUMERIC, Types.NUMERIC,
	 * Types.VARCHAR };
	 * 
	 * Long credentialValue = jdbcTemplateObject.queryForObject(
	 * CHECK_CREDENTIAL_SQL, paramList, argTypes, Long.class); logger.debug(
	 * "Available credential for the Request [{}]",credentialValue); if
	 * (credentialValue == 0) { throw new InvitationException(
	 * "Invalid Request-", null, "AC-0", HIDContants.INVALIDVALUE,
	 * HIDContants.BADREQUEST); } if (credentialValue == -1) { throw new
	 * InvitationException( "Invalid Request-", null, "AC-1",
	 * HIDContants.INVALIDVALUE, HIDContants.BADREQUEST); } if (credentialValue
	 * == -2) { throw new InvitationException( "Invalid Request-", null, "AC-2",
	 * HIDContants.INVALIDVALUE, HIDContants.BADREQUEST); } if (credentialValue
	 * == -3) { throw new InvitationException( "Invalid Request-", null, "AC-3",
	 * HIDContants.INVALIDVALUE, HIDContants.BADREQUEST); } if (credentialValue
	 * == -4) { throw new InvitationException( "Invalid Request-", null, "AC-4",
	 * HIDContants.INVALIDVALUE, HIDContants.BADREQUEST); } return
	 * credentialValue;
	 * 
	 * }
	 */

	/**
	 * This method interacts with database through query and get the Expire time
	 * for the companyId.
	 * 
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getExpiryTime(String companyId) {
		Long compId = new Long(companyId);
		Object[] paramList = new Object[] { compId };
		int[] argTypes = new int[] { Types.INTEGER };
		List<Map<String, Object>> outParamList = jdbcTemplateObject
				.queryForList(HIDContants.GET_EXPIRY_MINUTES_SQL, paramList,
						argTypes);
		return outParamList;

	}

	/**
	 * This method interacts with the database through query and make the status
	 * change for the given Invitation.
	 * 
	 * @param invitationId
	 */
	public void initateCancelInvitation(Long invitationId) {
		Object[] paramList = new Object[] { HIDContants.CANCEL_INITIATED,
				invitationId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		jdbcTemplateObject.update(HIDContants.INITIATE_INVITATION_CANCEL_SQL,
				paramList, argTypes);
	}

	/**
	 * This method interacts with database through Stored Procedure and get the
	 * Invitation Email details
	 * 
	 * @param companyId
	 * @param InvitationId
	 * @return
	 */
	public Map<String, Object> fetchSendInvitationDetails(String companyId,
			Long InvitationId) {
		Map<String, Object> inParams = new HashMap<String, Object>();
		Map<String, Object> outParams = new HashMap<String, Object>();
		Long compId = new Long(companyId);
		inParams.put("pi_company_id", compId);
		inParams.put("pi_aamk_invitation_id", InvitationId);
		SendInvitationEmail sendInvitationEmail = new SendInvitationEmail(
				this.dataSource);
		outParams = sendInvitationEmail.execute(inParams);
		return outParams;
	}

}
