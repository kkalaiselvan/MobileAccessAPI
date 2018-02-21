package com.hidglobal.managedservices.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hidglobal.managedservices.dao.MobileIdsDAO;
import com.hidglobal.managedservices.dao.UserDAO;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.UserException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.service.MobileAccessServices;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.SearchUserResponse;

/**
 * 
 * User Management Class acts as a mediator to PACS User requests. It has the
 * implementation of Business Logic of where the Request needs to delegated for
 * the operation to be executed successfully. This class handles the following
 * operations. 1. Enroll User 2. Update User 3. Get User Details 4. List User 5.
 * Delete User 6. Search User
 * 
 */

@Service
public class UserManagement implements IUserService {

	@Autowired
	private UserDAO userdao;
	@Autowired
	private MobileIdsDAO mobileIdsDAO;
	@Autowired
	private MobileAccessServices maServices;

	@Autowired
	IInvitationService invitationService;

	@Autowired
	MAValidator maValidator;

	@Autowired
	private PlatformTransactionManager transactionManager;

	private static final Logger logger = LoggerFactory
			.getLogger(UserManagement.class);

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * This method is used to enroll new user with or without invitation code
	 * depend on the request action. This method also holds the transaction
	 * control for user enrollment.
	 * 
	 * @param user
	 *            It comes with the user enrollment request along with the user
	 *            action.
	 * 
	 * @return This returns the user response with the userId and inventory
	 *         details, if any.
	 * 
	 * @throws Exception
	 */

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PacsUser enrollUser(PacsUser user) throws Exception {
		maValidator.validateInvitationUserActions(user.getUserActions());
		Long credentialValue = null;
		if ((user.getUserActions() != null)
				&& (user.getUserActions().getCreateInvitationCode() != null)
				&& (user.getUserActions().getAssignMobileId() != null)
				&& ("y".equalsIgnoreCase(user.getUserActions()
						.getCreateInvitationCode()))
				&& ("y".equalsIgnoreCase(user.getUserActions()
						.getAssignMobileId().toString()))) {
			credentialValue = mobileIdsDAO.checkAvailableCredential(
					user.getUserActions(), user.getCompanyId());
		}
		user = userdao.enrollUser(user);
		List<String> schemaList = new ArrayList<String>();
		schemaList.add(user.getSchemas().get(0).toString());
		if ((user.getUserActions() != null)
				&& (user.getUserActions().getCreateInvitationCode() != null)
				&& ("y".equalsIgnoreCase(user.getUserActions()
						.getCreateInvitationCode().toString()))) {
			PacsUser pacsUsers = new PacsUser();
			pacsUsers = invitationService.createInvitationForUser(user.getId(),
					user.getUserActions(), user.getCompanyId(),
					user.getUserName(), credentialValue);
			List<Invitation> invitationList = new ArrayList<Invitation>();
			invitationList.addAll(pacsUsers.getUserInvitations());
			user.setUserInvitations(invitationList);
			user.setUsermobileIds(pacsUsers.getUsermobileIds());
			user.setUserActions(null);

		}

		user.setUserActions(null);

		return user;

	}

	/**
	 * This method routes the update PACS User request to UserDAO class.
	 * 
	 * @param user
	 *            PACS User object to be updated.
	 */
	public PacsUser updateUser(PacsUser user) throws DBException,
			MobileAccessException {

		userdao.updateUser(user);
		return user;
	}

	public UserDAO getUserdao() {
		return userdao;
	}

	public void setUserdao(UserDAO userdao) {
		this.userdao = userdao;
	}

	/**
	 * This method routes the Get PACS User request to UserDAO class.
	 * 
	 * @param user
	 *            PACS User object to be updated.
	 */

	public PacsUser getUserDetails(PacsUser user) throws DBException, Exception {

		user = userdao.fetchUserDetails(user);
		return user;
	}

	/**
	 * This method routes the List PACS User request to UserDAO class.
	 * 
	 * @param filterByIds
	 *            QueryFilterId container which has company Id details.
	 * @return Lists PACS Users Meta information in JSON format.
	 */
	public SearchUserResponse listUsers(QueryFilterId filterByIds) {

		SearchUserResponse userList = userdao.listTopNUsers(filterByIds);
		return userList;
	}

	/**
	 * This method routes the Search User request to UserDAO class.
	 * 
	 * @param filterByIds
	 *            QueryFilterId container which has company Id details.
	 * @param searchRequest
	 *            SearchRequest which has search attributes, search filters,
	 *            count and sort criterias.
	 * @return
	 * 
	 *         Lists PACS Users with requested attributes and Meta Information
	 *         in JSON format.
	 */

	public SearchUserResponse searchUsers(SearchRequest searchRequest,
			QueryFilterId filterByIds) throws Exception {

		SearchUserResponse userResults = userdao.searchUser(
				searchRequest.getAttributes(), searchRequest.getFilterList(),
				filterByIds, searchRequest.getSortBy(),
				searchRequest.getSortingOrder(), searchRequest.getStartIndex(),
				searchRequest.getCount());
		return userResults;
	}

	/**
	 * This method implements the Business Logic to perform cascading delete of
	 * User Inventory upon performing Delete User.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * 
	 * @param companyUserId
	 *            The companyUserId who needs to be deleted.
	 * 
	 * @return Returns success or failure of delete user operation in Boolean.
	 */

	@SuppressWarnings("unchecked")
	@Override
	public int deleteUser(Long companyUserId, String companyId)
			throws UserException, Exception {

		Long userStatus = userdao.checkUserStatus(companyUserId, companyId);
		int resultsetStatus = HIDContants.USER_DELETE_INITIATED;
		if (userStatus != null) {

			List<Long> identifierId = new ArrayList<Long>();

			Map<String, Object> resultSetMap = null;
			identifierId.add(companyUserId);

			logger.info("Delete User Requested - company user Id[{}]",
					companyUserId);
			resultSetMap = userdao.getDeleteResultSet(identifierId, null,
					HIDContants.REQUESTOR, HIDContants.DELETE_USERS_IDENTIFIER);
			List<Invitation> invitationCursor = (List<Invitation>) resultSetMap
					.get("invitationCursor");

			List<MobileId> mobileIdCursor = (List<MobileId>) resultSetMap
					.get("mobileIdCursor");

			List<Device> deviceIdCursor = (List<Device>) resultSetMap
					.get("deviceCursor");
			BigDecimal retval = (BigDecimal) resultSetMap.get("retVal");
			Long[] invitationList = null;
			if (retval.intValue() == 0) {
				if ((invitationCursor != null) && !(invitationCursor.isEmpty())) {
					invitationList = new Long[invitationCursor.size()];

					for (int i = 0; i < invitationCursor.size(); i++) {
						invitationList[i] = invitationCursor.get(i)
								.getAamkInvitationId();

					}

				}

				List<Long> credIdList = null;
				if ((mobileIdCursor != null) && !(mobileIdCursor.isEmpty())) {

					Map<Long, List<Long>> deviceToMobileIdMap = new HashMap<Long, List<Long>>();

					for (MobileId mobileId : mobileIdCursor) {
						Long wsDeviceId = mobileId.getEndpointId();
						Long wsMobileId = mobileId.getId();
						credIdList = deviceToMobileIdMap.get(wsDeviceId);
						if (credIdList == null) {
							credIdList = new ArrayList<Long>();

						}
						credIdList.add(wsMobileId);
						deviceToMobileIdMap.put(wsDeviceId, credIdList);

					}
					resultSetMap.put("CREDENTIAL_MAP_KEY", deviceToMobileIdMap);

				}

				Long[] wsDeviceIds = null;

				if ((deviceIdCursor != null) && !(deviceIdCursor.isEmpty())) {

					wsDeviceIds = new Long[deviceIdCursor.size()];

					for (int i = 0; i < deviceIdCursor.size(); i++) {
						wsDeviceIds[i] = deviceIdCursor.get(i).getId();
					}
				}
				if (!invitationCursor.isEmpty()) {
					maServices.cancelInvitation(invitationList);
				}
				if (!mobileIdCursor.isEmpty()) {
					maServices.revokeCredentialProcess(resultSetMap);
				}
				if (!deviceIdCursor.isEmpty()) {
					maServices.deleteDevice(wsDeviceIds);
				}
				if ((deviceIdCursor.isEmpty()) && (mobileIdCursor.isEmpty())
						&& (invitationCursor.isEmpty())) {
					resultsetStatus = HIDContants.USER_DELETED;
				}
				logger.info("Status of the user requested [{}]",
						resultsetStatus);
				return resultsetStatus;
			}
		}

		return resultsetStatus;
	}

}
