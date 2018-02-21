package com.hidglobal.managedservices.business;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hidglobal.managedservices.dao.InvitationDAO;
import com.hidglobal.managedservices.dao.MobileIdsDAO;
import com.hidglobal.managedservices.dao.UserDAO;
import com.hidglobal.managedservices.dao.UtilsDAO;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.service.MobileAccessServices;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.InvitationEmailComposer;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.PacsUserActions;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchInvitationResponse;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.UserInvitations;

/**
 * 
 * This class act as a Mediator for all the Invitation related requests. It has
 * the implementation of Business Logic of where the Request needs to delegated
 * for the operation to be executed successfully.This class handles the
 * following requests 1.Get Invitation 2.List Invitation 3.Search Invitation
 * 4.create Invitation 5.cancel Invitation
 * 
 */
@Service
public class InvitationManagement implements IInvitationService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private MobileIdsDAO mobileIdsDAO;
	@Autowired
	private InvitationDAO invitationDAO;
	@Autowired
	private MobileAccessServices ma_WebService;
	@Autowired
	private MAValidator maValidator;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private InvitationEmailComposer invitationEmailComposer;
	@Autowired
	private UtilsDAO utilsDAO;

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;

	private static final Logger logger = LoggerFactory
			.getLogger(InvitationManagement.class);

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * This method routes Get Invitation requests to InvitationDAO class
	 * 
	 * @param filters
	 *            This holds the Invitation Id
	 */
	public Invitation getInvitationDetails(QueryFilterId filters)
			throws DBException {

		return invitationDAO.fetchInvitationDetails(filters);
	}

	/**
	 * This method routes List Invitation requests to InvitationDAO class.
	 * 
	 * @param filters
	 *            This holds the userId.
	 */
	public SearchInvitationResponse listInvitation(QueryFilterId filters) {
		// TODO Auto-generated method stub
		return invitationDAO.listInvitations(filters);
	}

	/**
	 * This method routes Search Invitation requests to InvitationDAO class
	 * 
	 * @param searchRequest
	 *            This holds search attributes, search filters, count and sort
	 *            criterias.
	 * @param filterByIds
	 * 
	 */
	public SearchInvitationResponse searchInvitation(
			SearchRequest searchRequest, QueryFilterId filterByIds)
			throws Exception {
		// TODO Auto-generated method stub
		SearchInvitationResponse invitationResponse = invitationDAO
				.searchInvitation(searchRequest.getAttributes(),
						searchRequest.getFilterList(), filterByIds,
						searchRequest.getSortBy(),
						searchRequest.getSortingOrder(),
						searchRequest.getStartIndex(), searchRequest.getCount());
		return invitationResponse;
	}

	/**
	 * This method routes the cancel Invitation requests to InvitationDAO and
	 * hence on valid response it route to MobileAccessServices to cancel the
	 * Invitation
	 * 
	 * @param invitationId
	 * @param companyId
	 */
	public boolean cancelInvitation(long invitationId, String companyId)
			throws InvitationException, MobileAccessException,
			MAValidationException {
		Long compId = new Long(companyId);
		Long status = invitationDAO.checkInvitationStatus(invitationId, compId);
		invitationDAO.initateCancelInvitation(invitationId);
		if (status == HIDContants.INVITATION_PENDING_STATUS) {
			Long[] invitationList = new Long[1];
			invitationList[0] = invitationId;
			ma_WebService.cancelInvitation(invitationList);
			return true;
		}
		return false;
	}

	/*
	 * public boolean cancelInvitationForUser(long userId, long invitationId,
	 * String companyId) throws InvitationException, InvitationException,
	 * MobileAccessException { Long compId = new Long(companyId); Long status =
	 * invitationDAO.checkInvitationStatusForUser(userId, invitationId, compId);
	 * invitationDAO.initateCancelInvitation(invitationId); if (status ==
	 * HIDContants.INVITATION_PENDING_STATUS) { Long[] invitationList = new
	 * Long[1]; invitationList[0] = invitationId;
	 * ma_WebService.cancelInvitation(invitationList); return true; } return
	 * false; }
	 */

	/**
	 * This method will route the requests to userDAO to check the user status.
	 * If the user is Active , then it will make a call to web service to create
	 * Invitation.On successful response, the invitation details are routed to
	 * InvitationDAO to save the Information in database.If Send Invitation is
	 * "y" it will send an Invitation to the requested user.
	 * 
	 * @param companyUserId
	 * @param pacsUserAction
	 *            This holds the User Action Details
	 * @param companyId
	 * @param requestor
	 */

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PacsUser createInvitation(long companyUserId,
			PacsUserActions pacsUserAction, String companyId, String requestor)
			throws InvitationException, Exception {
		PacsUser pacsUser = new PacsUser();
		MobileId mobileId = new MobileId();
		Meta meta = new Meta();
		UserInvitations userInvitation = new UserInvitations();
		Invitation invitation;
		Long credentialValue = null;
		maValidator.validateInvitationActions(pacsUserAction);
		userDAO.checkUserStatus(companyUserId, companyId);

		if (!(pacsUserAction == null)
				&& (pacsUserAction.getAssignMobileId() != null)
				&& "y".equalsIgnoreCase(pacsUserAction.getAssignMobileId())) {

			credentialValue = mobileIdsDAO.checkAvailableCredential(
					pacsUserAction, companyId);

			if (!(credentialValue == 0)) {
				logger.info(
						"Credential Value for this  auto assign action request [{}] ",
						credentialValue);
				List<Map<String, Object>> expiryTime = invitationDAO
						.getExpiryTime(companyId);
				Integer expiryTimeinMinutes = getInvitationExpiryTimeForCompanyInMin(expiryTime);
				logger.info(
						"Expiry time for this create invitation request [{}]",
						expiryTimeinMinutes);
				invitation = ma_WebService.createInvitation(expiryTimeinMinutes
						.longValue());
				List<Invitation> invitationList = new ArrayList<Invitation>();
				invitationList.add(invitation);
				userInvitation.setInvitations(invitationList);
				List<MobileId> mobileIdList = new ArrayList<MobileId>();
				mobileIdList.add(mobileId);
				userInvitation.setMobileId(mobileIdList);
				userInvitation.setMeta(meta);
				userInvitation = invitationDAO.createInvitation(companyUserId,
						userInvitation, pacsUserAction, 
						expiryTimeinMinutes, companyId, requestor);

				if ((pacsUserAction !=null)&&(pacsUserAction.getSendInvitationEmail() != null)
						&& ("y".equalsIgnoreCase(pacsUserAction
								.getSendInvitationEmail().toString()))) {
					utilsDAO.fetchHostDetails();
					invitationEmailComposer.sendInvitation(companyId,
							invitation.getAamkInvitationId());
				}

				/**
				 * Sets Meta Information
				 */
				meta.setResourceType(HIDContants.INVITATIONRESOURCETYPE);
				meta.setLocation(resourceLocation
						.getProperty(HIDContants.INVITATIONLOCATION)
						.concat("/")
						.concat(String.valueOf(invitation.getAamkInvitationId())));
				meta.setLastModified(userInvitation.getMeta().getLastModified());
				String resourceUrl = MessageFormat.format(meta.getLocation(),
						new Long(companyId).toString());
				meta.setLocation(resourceUrl);
				invitation.setMeta(meta);
				List<Invitation> invitationsList = new ArrayList<Invitation>();
				invitationsList.add(invitation);
				pacsUser.setUserInvitations(invitationsList);
				pacsUser.setUsermobileIds(userInvitation.getMobileId());
				List<String> schemas = new ArrayList<String>();
				/**
				 * Sets Schema Information
				 */
				schemas.add(Schemas.INVITATIONS.toString());
				schemas.add(Schemas.MOBILE_ID.toString());
				pacsUser.setSchemas(schemas);
				return pacsUser;

			}
			return pacsUser;

		} else {
			List<Map<String, Object>> expiryTime = invitationDAO
					.getExpiryTime(companyId);
			Integer expiryTimeinMinutes = getInvitationExpiryTimeForCompanyInMin(expiryTime);
			logger.info("Expiry time for this create invitation request [{}]",
					expiryTimeinMinutes);
			invitation = ma_WebService.createInvitation(expiryTimeinMinutes
					.longValue());
			List<Invitation> invitationList = new ArrayList<Invitation>();
			invitationList.add(invitation);
			userInvitation.setInvitations(invitationList);
			List<MobileId> mobileIdList = new ArrayList<MobileId>();
			mobileIdList.add(mobileId);
			userInvitation.setMobileId(mobileIdList);
			userInvitation.setMeta(meta);
			userInvitation = invitationDAO.createInvitation(companyUserId,
					userInvitation, pacsUserAction,  expiryTimeinMinutes,
					companyId, requestor);

			/**
			 * Sets Meta Information
			 */

			if ((pacsUserAction !=null)&&(pacsUserAction.getSendInvitationEmail() != null)
					&& ("y".equalsIgnoreCase(pacsUserAction
							.getSendInvitationEmail().toString()))) {
				utilsDAO.fetchHostDetails();
				invitationEmailComposer.sendInvitation(companyId,
						invitation.getAamkInvitationId());
			}
			meta.setResourceType(HIDContants.INVITATIONRESOURCETYPE);
			meta.setLocation(resourceLocation
					.getProperty(HIDContants.INVITATIONLOCATION).concat("/")
					.concat(String.valueOf(invitation.getAamkInvitationId())));
			meta.setLastModified(userInvitation.getMeta().getLastModified());
			String resourceUrl = MessageFormat.format(meta.getLocation(),
					new Long(companyId).toString());
			meta.setLocation(resourceUrl);
			invitation.setMeta(meta);

			List<Invitation> invitationsList = new ArrayList<Invitation>();
			invitationsList.add(invitation);
			pacsUser.setUserInvitations(invitationsList);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.INVITATIONS.toString());
			pacsUser.setSchemas(schemas);
			return pacsUser;

		}

	}
	/**
	 * This method(Part of Enroll User) will route the requests to userDAO to check the user status.
	 * If the user is Active , then it will make a call to web service to create
	 * Invitation.On successful response, the invitation details are routed to
	 * InvitationDAO to save the Information in database.If Send Invitation is
	 * "y" it will send an Invitation to the requested user     .
	 * 
	 * @param companyUserId
	 * @param pacsUserAction
	 *            This holds the User Action Details
	 * @param companyId
	 * @param requestor
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PacsUser createInvitationForUser(long companyUserId,
			PacsUserActions pacsUserAction, String companyId, String requestor,
			Long credentialValue) throws InvitationException, Exception {
		PacsUser pacsUser = new PacsUser();
		MobileId mobileId = new MobileId();
		Meta meta = new Meta();
		UserInvitations userInvitation = new UserInvitations();
		Invitation invitation;
		maValidator.validateInvitationActions(pacsUserAction);
		userDAO.checkUserStatus(companyUserId, companyId);

		if (!(pacsUserAction == null)
				&& (pacsUserAction.getAssignMobileId() != null)
				&& "y".equalsIgnoreCase(pacsUserAction.getAssignMobileId())) {
			if ((credentialValue != null) && !(credentialValue == 0)) {
				logger.info(
						"Credential Value for this  auto assign action request [{}] ",
						credentialValue);
				List<Map<String, Object>> expiryTime = invitationDAO
						.getExpiryTime(companyId);
				Integer expiryTimeinMinutes = getInvitationExpiryTimeForCompanyInMin(expiryTime);
				logger.info(
						"Expiry time for this create invitation request [{}]",
						expiryTimeinMinutes);
				invitation = ma_WebService.createInvitation(expiryTimeinMinutes
						.longValue());
				List<Invitation> invitationList = new ArrayList<Invitation>();
				invitationList.add(invitation);
				userInvitation.setInvitations(invitationList);
				List<MobileId> mobileIdList = new ArrayList<MobileId>();
				mobileIdList.add(mobileId);
				userInvitation.setMobileId(mobileIdList);
				userInvitation.setMeta(meta);
				userInvitation = invitationDAO.createInvitation(companyUserId,
						userInvitation, pacsUserAction, 
						expiryTimeinMinutes, companyId, requestor);

				if ((pacsUserAction.getSendInvitationEmail() != null)
						&& ("y".equalsIgnoreCase(pacsUserAction
								.getSendInvitationEmail().toString()))) {
					utilsDAO.fetchHostDetails();
					invitationEmailComposer.sendInvitation(companyId,
							invitation.getAamkInvitationId());
				}

				/**
				 * Sets Meta Information
				 */
				meta.setResourceType(HIDContants.INVITATIONRESOURCETYPE);
				meta.setLocation(resourceLocation
						.getProperty(HIDContants.INVITATIONLOCATION)
						.concat("/")
						.concat(String.valueOf(invitation.getAamkInvitationId())));
				meta.setLastModified(userInvitation.getMeta().getLastModified());
				invitation.setMeta(meta);
				List<Invitation> invitationsList = new ArrayList<Invitation>();
				invitationsList.add(invitation);
				pacsUser.setUserInvitations(invitationsList);
				pacsUser.setUsermobileIds(userInvitation.getMobileId());
				List<String> schemas = new ArrayList<String>();
				/**
				 * Sets Schema Information
				 */
				schemas.add(Schemas.INVITATIONS.toString());
				schemas.add(Schemas.MOBILE_ID.toString());
				pacsUser.setSchemas(schemas);
				return pacsUser;

			}
			return pacsUser;

		} else {
			List<Map<String, Object>> expiryTime = invitationDAO
					.getExpiryTime(companyId);
			Integer expiryTimeinMinutes = getInvitationExpiryTimeForCompanyInMin(expiryTime);
			logger.info("Expiry time for this create invitation request [{}]",
					expiryTimeinMinutes);
			invitation = ma_WebService.createInvitation(expiryTimeinMinutes
					.longValue());
			List<Invitation> invitationList = new ArrayList<Invitation>();
			invitationList.add(invitation);
			userInvitation.setInvitations(invitationList);
			List<MobileId> mobileIdList = new ArrayList<MobileId>();
			mobileIdList.add(mobileId);
			userInvitation.setMobileId(mobileIdList);
			userInvitation.setMeta(meta);
			userInvitation = invitationDAO.createInvitation(companyUserId,
					userInvitation, pacsUserAction,  expiryTimeinMinutes,
					companyId, requestor);

			/**
			 * Sets Meta Information
			 */

			if ((pacsUserAction!=null)&&(pacsUserAction.getSendInvitationEmail() != null)
					&& ("y".equalsIgnoreCase(pacsUserAction
							.getSendInvitationEmail().toString()))) {
				utilsDAO.fetchHostDetails();
				invitationEmailComposer.sendInvitation(companyId,
						invitation.getAamkInvitationId());
			}
			meta.setResourceType(HIDContants.INVITATIONRESOURCETYPE);
			meta.setLocation(resourceLocation
					.getProperty(HIDContants.INVITATIONLOCATION).concat("/")
					.concat(String.valueOf(invitation.getAamkInvitationId())));
			meta.setLastModified(userInvitation.getMeta().getLastModified());
			invitation.setMeta(meta);

			List<Invitation> invitationsList = new ArrayList<Invitation>();
			invitationsList.add(invitation);
			pacsUser.setUserInvitations(invitationsList);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.INVITATIONS.toString());
			pacsUser.setSchemas(schemas);
			return pacsUser;

		}

	}

	public Integer getInvitationExpiryTimeForCompanyInMin(
			List<Map<String, Object>> expiryTime) throws Exception {

		Integer expirationTimeinMinutes = Integer.parseInt(expiryTime.get(0)
				.get("VALUE").toString());
		Integer expirationTimeUnits = Integer.parseInt(expiryTime.get(1)
				.get("VALUE").toString());

		Integer expirationTimeinMin = null;
		Integer expirationTimeUnit = null;

		if (expirationTimeinMinutes != null) {
			expirationTimeinMin = new Integer(
					expirationTimeinMinutes.toString());
			// #4497 is for Hours #4498 for Days
			if (expirationTimeUnits != null) {
				expirationTimeUnit = new Integer(expirationTimeUnits.toString());
				if (expirationTimeUnit.intValue() == 4497) {
					// convert hours to minutes
					expirationTimeinMin = expirationTimeinMin * 60;
				} else {
					// convert days to minutes
					expirationTimeinMin = expirationTimeinMin * 24 * 60;
				}
			} else {
				// convert days to minutes
				expirationTimeinMin = expirationTimeinMin * 24 * 60;
			}
		}
		return expirationTimeinMin;

	}

	public void fetchSendInvitationDetails(String companyId, Long invitationId)
			throws Exception {
		Long compId = new Long(companyId);
		Long status = invitationDAO.checkInvitationStatus(invitationId, compId);

		if (status == HIDContants.INVITATION_PENDING_STATUS) {
			invitationEmailComposer.sendInvitation(companyId, invitationId);

		}
	}

}
