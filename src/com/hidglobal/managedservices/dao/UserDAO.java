package com.hidglobal.managedservices.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.UserException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.mapper.UserRowMapper;
import com.hidglobal.managedservices.proc.PacsCompanyUserDelete;
import com.hidglobal.managedservices.proc.PacsCompanyUserUpsert;
import com.hidglobal.managedservices.proc.PacsGetUser;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.QueryBuilder;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.Name;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.QueryBuilderResponse;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchUserResponse;
import com.hidglobal.managedservices.vo.Schema.Schemas;

/**
 * This class performs CRUD operations for the entity PACS User.
 * 
 */

@Repository
public class UserDAO extends AbstractDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	@Autowired
	QueryBuilder queryBuilder;
	@Autowired
	UtilsDAO utilsDAO;

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}

	public Meta setMeta(String id, String lastModifiedDt) {
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.USERRESOURCETYPE);
		meta.setLocation(resourceLocation.getProperty(HIDContants.USERLOCATION)
				.concat("/").concat(id));
		meta.setLastModified(lastModifiedDt);
		return meta;

	}

	/**
	 * This method performs an insert operation on Mobile Access DB.
	 * 
	 * @param user
	 *            PACS User object that needs to be inserted.
	 * @return Returns PACS User object that is inserted.
	 * 
	 * @throws DBException
	 *             Throws DB Exception when any violation of DB constraints.
	 * @throws MobileAccessException
	 *             Throws MobileAccessException when unexpected error happens at
	 *             the DB.
	 * 
	 */
	public PacsUser enrollUser(PacsUser user) throws DBException,
			MobileAccessException {
		// TODO Auto-generated method stub

		Map<String, Object> inParams = new HashMap<String, Object>();
		Map<String, Object> outParams;

		userUpsertMapper(user, inParams);

		logger.info("Enroll User Input Params: [{}] ", inParams);

		PacsCompanyUserUpsert insertCompanyUser = new PacsCompanyUserUpsert(
				jdbcTemplateObject.getDataSource());

		outParams = insertCompanyUser.execute(inParams);

		logger.info("Enroll User Output Params: [{}]", outParams);

		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();
		String po_retmsg = (String) outParams.get("po_retmsg");

		if (po_retval == 0) {

			BigDecimal companyUserId = (BigDecimal) outParams
					.get("pio_company_user_id");
			logger.info(
					"Pacs User Enrolled Successfully. Company User ID:[{}] ",
					companyUserId);
			user.setId(companyUserId.longValue());
			user.setLastModifiedDt((String) outParams
					.get("po_last_modified_dt"));
			user.setStatus(outParams.get("po_status").toString());
			Meta meta = setMeta(String.valueOf(user.getId()),
					String.valueOf(user.getLastModifiedDt()));
			user.setMeta(meta);
		}

		else if (po_retval == HIDContants.DUPLICATE_USER) {

			logger.error(
					"Enroll User Failed. User with email id [{}] already exists",
					user.getEmails().get(0).getValue());
			throw new DBException(inParams.get("pio_email").toString(),
					Integer.toString(po_retval), HIDContants.UNIQUNESS);
		} else if (po_retval == HIDContants.USERID_MANDATORY) {
			logger.error("Update User Failed. User Id Mandatory");
			throw new DBException(null, Integer.toString(po_retval),
					HIDContants.INVALIDVALUE);
		}

		else {
			logger.error(
					"Enroll User Failed. Mobile Access Databse Exception: Error Code: [{}], Error Details: [{}]",
					po_retval, po_retmsg);
			throw new MobileAccessException(po_retmsg);
		}

		return user;
	}

	/**
	 * Creates a Map to perform insert to database.
	 * 
	 * @param user
	 * @param inParams
	 */
	private void userUpsertMapper(PacsUser user, Map<String, Object> inParams) {

		inParams.put("pio_company_user_id", user.getId());
		inParams.put("pio_first_name", user.getName().getGivenName());
		inParams.put("pio_last_name", user.getName().getFamilyName());
		inParams.put("pio_email", user.getEmails().get(0).getValue());
		inParams.put("pio_user_id", user.getExternalId());
		inParams.put("pio_company_id", user.getCompanyId());
		inParams.put("pio_requestor", user.getUserName());

	}

	/**
	 * Creates a Map that can be passed for the Delete User Procedure
	 * 
	 * @param user
	 * @param inParams
	 * 
	 * @throws Exception
	 */

	public Map<String, Object> getDeleteResultSet(List<Long> identifierId,
			List<Long> endpointId, String userName, String identifier)
			throws Exception {
		Map<String, Object> inParams = new HashMap<String, Object>();
		Map<String, Object> outParams;
		Connection connection = null;
		try {
			ARRAY userArrayDB = null;
			ARRAY endPointArrayDB = null;
			Number[] userArray = identifierId.toArray(new Number[] {});
			Number[] endPointsArray = new Number[0];
			if (!HIDContants.DELETE_USERS_IDENTIFIER
					.equalsIgnoreCase(identifier)) {
				endPointsArray = endpointId.toArray(new Number[] {});
			} else {
				endPointsArray = new Number[0];
			}
			connection = DataSourceUtils.getConnection(dataSource);
			ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor(
					"NUMBER_TYPE", connection);
			userArrayDB = new ARRAY(arrayDesc, connection, userArray);
			endPointArrayDB = new ARRAY(arrayDesc, connection, endPointsArray);
			inParams.put("identifier", identifier);
			inParams.put("userArrayDB", userArrayDB);
			inParams.put("endPointArrayDB", endPointArrayDB);
			inParams.put("userName", userName);
			logger.info("Delete  User Input Params: [{}] ", inParams);
			PacsCompanyUserDelete pacsCompanyUserDeleteProc = new PacsCompanyUserDelete(
					this.dataSource);
			outParams = pacsCompanyUserDeleteProc.execute(inParams);
			logger.info("Delete  User Output Params: [{}] ", outParams);
			return outParams;

		} catch (Exception e) {
			logger.error("Error occured in Delete User" + e);
			throw new MobileAccessException(e.getMessage());
		} finally {
			connection.close();
		}

	}

	/**
	 * This method performs an update operation on Mobile Access DB.
	 * 
	 * @param user
	 *            PACS User data that needs to be updated
	 * @return Returns PACS User object that is inserted.
	 * 
	 * @throws DBException
	 *             Throws DB Exception when any violation of DB constraints.
	 * @throws MobileAccessException
	 *             Throws MobileAccessException when unexpected error happens at
	 *             the DB.
	 */
	public PacsUser updateUser(PacsUser user) throws DBException,
			MobileAccessException {

		Map<String, Object> inParams = new HashMap<String, Object>();
		userUpsertMapper(user, inParams);
		logger.info("Update User Input Params, [{}]", inParams);

		PacsCompanyUserUpsert updateCompanyUser = new PacsCompanyUserUpsert(
				jdbcTemplateObject.getDataSource());

		Map<String, Object> outParams = updateCompanyUser.execute(inParams);
		logger.info("Update User Output Params, [{}]", outParams);
		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();
		String po_retmsg = (String) outParams.get("po_retmsg");

		if (po_retval == 0) {
			logger.info("Update User Successful for User, [{}]", user.getId());
			user.setLastModifiedDt((String) outParams
					.get("po_last_modified_dt"));
			user.setStatus(outParams.get("po_status").toString());
			Meta meta = setMeta(String.valueOf(user.getId()),
					String.valueOf(user.getLastModifiedDt()));
			user.setMeta(meta);
		}

		else if (po_retval == HIDContants.DUPLICATE_USER) {
			logger.error(
					"Update User Failed. User with email id [{}] already exists",
					user.getEmails().get(0).getValue());
			throw new DBException(inParams.get("pio_email").toString(),
					Integer.toString(po_retval), HIDContants.UNIQUNESS);
		} else if (po_retval == HIDContants.INVALID_USER) {
			logger.error("Update User Failed. User with Id [{}] Not Found",
					user.getId());
			throw new DBException(String.valueOf(user.getId()),
					Integer.toString(po_retval), HIDContants.NOTARGET);
		} else if (po_retval == HIDContants.USERID_MANDATORY) {
			logger.error("Update User Failed. User Id Mandatory");
			throw new DBException(null, Integer.toString(po_retval),
					HIDContants.INVALIDVALUE);
		} else if (po_retval == HIDContants.DELETING_USER) {
			logger.error("Update User Failed. User with Id [{}] is in deleting state");
			throw new DBException(null, Integer.toString(po_retval),
					HIDContants.MUTABILITY);
		}
		else {
			logger.error(
					"Update User Failed. Mobile Access Databse Exception: Error Code: [{}], Error Details: [{}]",
					po_retval, po_retmsg);
			throw new MobileAccessException(po_retmsg);
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public PacsUser fetchUserDetails(PacsUser user) throws DBException,
			Exception {
		/**
		 * Company User Id and Company Id should be replaced with oAuth Token
		 */

		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("pio_company_id", user.getCompanyId());
		inParams.put("pio_company_user_id", user.getId());
		inParams.put("pi_requestor", user.getUserName());
		PacsGetUser getUser = new PacsGetUser(
				jdbcTemplateObject.getDataSource());
		Map<String, Object> outParams = getUser.execute(inParams);
		System.out.println("OutpArams for Get User::" + outParams);
		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();
		String po_retmsg = (String) outParams.get("po_retmsg");

		if (po_retval == 0) {
			logger.info("Fetch User Details Successful. User Details: [{}]"
					+ outParams);

			/**
			 * Map User Details
			 */

			List<PacsUser> users = new ArrayList<PacsUser>();
			users = (List<PacsUser>) outParams.get("po_user_result_set");
			user = users.get(0);

			/**
			 * Map Invitation Details
			 */

			List<Invitation> invitations = new ArrayList<Invitation>();
			invitations = (List<Invitation>) outParams
					.get("po_invitation_result_set");
			if (invitations.size() > 0) {
				for (Invitation invitation : invitations) {
					invitation.setMeta(null);
				}
				user.setUserInvitations(invitations);
			}
			/**
			 * Map Devices and Mobile Ids
			 */

			Map<String, Device> endpointCredentialMap = new HashMap<String, Device>();
			List<Device> userDevices = new ArrayList<Device>();
			List<MobileId> mobileIds = null;
			MobileId mobileId = null;
			userDevices = (List<Device>) outParams
					.get("po_endpt_credential_result_set");
			if (userDevices.size() > 0) {
				for (Device device : userDevices) {
					logger.info("Device Endpoint ID [{}] found for user [{}] ",
							user.getId(), device.getId());

					mobileId = device.getMobileId();

					logger.info("Mobile ID [{}] found for Device [{}]",
							mobileId.getId(), device.getId());

					if (endpointCredentialMap
							.get(String.valueOf(device.getId())) == null) {
						mobileIds = new ArrayList<MobileId>();
					}

					if (device.getMobileId().getId() != null) {

						mobileIds.add(device.getMobileId());
					}

					if (!(mobileIds == null || mobileIds.isEmpty()))
						device.setMobileIds(mobileIds);

					endpointCredentialMap.put(String.valueOf(device.getId()),
							device);

				}

				userDevices = new ArrayList<Device>();

				for (Map.Entry<String, Device> entry : endpointCredentialMap
						.entrySet()) {
					userDevices.add(entry.getValue());
				}

				user.setUserDevices(userDevices);
			}
			return user;
		}

		if (po_retval == HIDContants.INVALID_USER) {
			logger.error("Get User Details Failed for User ID [{}]",
					user.getId());

			throw new DBException(String.valueOf(user.getId()),
					Integer.toString(po_retval), HIDContants.NOTARGET);
		}

		else {
			logger.error(
					"Update User Failed. Mobile Access Databse Exception: Error Code: [{}], Error Details: [{}]",
					po_retval, po_retmsg);
			throw new MobileAccessException(po_retmsg);
		}

	}

	/**
	 * Lists the last created/modified PACS User records.
	 * 
	 * @param filterByIds
	 *            Container to hold Company Id.
	 * @return
	 */

	public SearchUserResponse listTopNUsers(QueryFilterId filterByIds) {

		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.PACSUSER,
				filterByIds);

		int count = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.info("Total Users for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), count);
		List<PacsUser> userRowObject = getUserResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());

		SearchUserResponse userList = new SearchUserResponse();
		List<PacsUser> users = new ArrayList<PacsUser>();

		for (PacsUser user : userRowObject) {
			PacsUser pacsUser = new PacsUser();
			pacsUser.setMeta(user.getMeta());
			pacsUser.getMeta().setLocation(
					MessageFormat.format(pacsUser.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			users.add(pacsUser);
		}

		userList.setTotalResults(count);
		userList.setItemsPerPage(users.size());
		userList.setStartIndex(QueryBuilder.START_INDEX);
		logger.info("Items Per Page: [{}]", userList.getItemsPerPage());
		logger.info("Start Index: [{}]", userList.getItemsPerPage());
		userList.setUsers(users);
		return userList;
	}

	/**
	 * This method performs a sql query on Mobile Access DB
	 * 
	 * @param sqlQuery
	 * @param paramsList
	 * @return Returns PACS User Row Object
	 */
	private List<PacsUser> getUserResultSet(String sqlQuery,
			List<Object> paramsList) {

		List<PacsUser> userRowObject = new ArrayList<PacsUser>();
		userRowObject = (List<PacsUser>) jdbcTemplateObject.query(sqlQuery,
				paramsList.toArray(), new UserRowMapper());
		return userRowObject;
	}

	/**
	 * This method performs Select Operation on the Mobile Access DB.
	 * 
	 * @param queryAttributes
	 *            Columns that needs to be retrieved
	 * @param queryFilters
	 *            Query Filter conditions
	 * @param filterByIds
	 *            Container to hold Company Id, Company User Id.
	 * @param sortBy
	 *            Sort by parameter
	 * @param sortOrder
	 *            Sort Order. Ascending or Descending.
	 * @param startIndex
	 *            Row Number from which record should be retrieved.
	 * @param maxCount
	 *            Maximum
	 * @return
	 * @throws Exception
	 */
	public SearchUserResponse searchUser(List<String> queryAttributes,
			String queryFilters, QueryFilterId filterByIds, String sortBy,
			String sortOrder, int startIndex, int maxCount) throws Exception {

		QueryBuilderResponse builderResponse;
		startIndex = (startIndex < 1) ? QueryBuilder.START_INDEX : startIndex;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.PACSUSER,
				queryFilters, filterByIds, sortBy, sortOrder, startIndex,
				maxCount);

		int count = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.info("Total Users for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), count);
		System.out.println("QUery Param Size = "
				+ builderResponse.getQueryParams().size());
		List<PacsUser> userRowObject = getUserResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());

		SearchUserResponse userSearchResult = new SearchUserResponse();
		List<PacsUser> users = new ArrayList<PacsUser>();

		for (PacsUser user : userRowObject) {
			PacsUser pacsUser = new PacsUser();
			Name name = new Name();
			if (queryAttributes != null) {
				logger.info("Requested Query Attributes: [{}]", queryAttributes);
				for (String attribute : queryAttributes) {

					if (HIDContants.EMAIL.equalsIgnoreCase(attribute)
							|| HIDContants.EMAIL_VALUE
									.equalsIgnoreCase(attribute)) {
						pacsUser.setEmails(user.getEmails());
					} else if (HIDContants.STATUS.equalsIgnoreCase(attribute)) {
						pacsUser.setStatus(user.getStatus());
					} else if (HIDContants.GIVEN_NAME
							.equalsIgnoreCase(attribute)) {
						name.setGivenName(user.getName().getGivenName());
					} else if (HIDContants.FAMILY_NAME
							.equalsIgnoreCase(attribute)) {
						name.setFamilyName(user.getName().getFamilyName());
					}
					pacsUser.setName(name);
				}
			}
			if ((pacsUser.getName() != null)
					&& (pacsUser.getName().getFamilyName() == null)
					&& (pacsUser.getName().getGivenName() == null))
				pacsUser.setName(null);

			pacsUser.setMeta(user.getMeta());
			pacsUser.getMeta().setLocation(
					MessageFormat.format(pacsUser.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			users.add(pacsUser);

		}

		userSearchResult.setTotalResults(count);
		userSearchResult.setItemsPerPage(users.size());
		userSearchResult.setStartIndex(startIndex);
		logger.info("Items Per Page: [{}]", userSearchResult.getItemsPerPage());
		logger.info("Start Index: [{}]", userSearchResult.getItemsPerPage());
		userSearchResult.setUsers(users);
		return userSearchResult;
	}

	/**
	 * This method checks the user status by passing UserId and companyId
	 * 
	 * @param companyUserId
	 * @param companyId
	 * @return This returns the status of the requested user
	 * @throws UserException
	 */
	public Long checkUserStatus(Long companyUserId, String companyId)
			throws UserException {
		Long compId = new Long(companyId);
		Object[] paramObject = new Object[] { companyUserId, compId };
		int[] argType = new int[] { Types.INTEGER, Types.INTEGER };
		Long userStatus = null;
		/* String forceDeleteStatus=null; */
		try {

			Map<String, Object> results = getJdbcTemplateObject().queryForMap(
					HIDContants.USER_STATUS_SQL, paramObject, argType);

			if (results.get("STATUS_ID") != null) {
				userStatus = new Long(results.get("STATUS_ID").toString());
			} else {
				throw new UserException(companyUserId.toString(), null,
						"invalid.user", HIDContants.NOTARGET,
						HIDContants.REQUESTNOTFOUND);
			}
			/*
			 * if (results.get("IS_FORCE_DELETED") != null) { forceDeleteStatus
			 * = results.get("IS_FORCE_DELETED").toString();
			 * 
			 * }
			 */

			logger.info("User Status for the request [{}]", userStatus);

			if ((userStatus == HIDContants.USER_DELETED)
			/*
			 * || (userStatus == HIDContants.USER_DELETE_INITIATED) &&
			 * (forceDeleteStatus. equalsIgnoreCase("Y"))
			 */) {

				throw new UserException(companyUserId.toString(), null,
						"invalid.user", HIDContants.NOTARGET,
						HIDContants.REQUESTNOTFOUND);
			}
			if (userStatus == HIDContants.USER_DELETE_INITIATED) {

				throw new UserException(companyUserId.toString(), null,
						"invalid.user.deleting", HIDContants.MUTABILITY,
						HIDContants.PRECONDITIONFAILED);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new UserException(companyUserId.toString(), null,
					"invalid.user", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}

		return userStatus;
	}

	/**
	 * This method interacts with database and fetch the company userId and
	 * endpoint status id from device endpointId
	 * 
	 * @param deviceId
	 * @param companyId
	 * @return company user id
	 * @throws Exception
	 */
	public Long fetchCompanyUserIdfromEndpoint(Long deviceId, Long companyId)
			throws Exception {
		Object[] paramList = new Object[] { deviceId, companyId };
		int[] argTypes = new int[] { Types.NUMERIC, Types.NUMERIC };
		Long companyUserId;
		Long endpointStatus;
		try {
			List<Map<String, Object>> results = getJdbcTemplateObject().queryForList(
					HIDContants.GET_COMPANY_USERID_SQL, paramList, argTypes);
		
			companyUserId = new Long(results.get(0).get("COMPANY_USER_ID").toString());
			endpointStatus = new Long(results.get(0).get("ENDPOINT_STATUS_ID")
					.toString());
			if (endpointStatus == 654 || endpointStatus == 655) {
				throw new MobileIdException(deviceId.toString(), null,
						"-20091", HIDContants.MUTABILITY,
						HIDContants.PRECONDITIONFAILED);
			}
			if (endpointStatus == 656) {
				throw new MobileIdException(deviceId.toString(), null,
						"invalid.device.deleting", HIDContants.MUTABILITY,
						HIDContants.PRECONDITIONFAILED);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new MobileIdException(deviceId.toString(), null,
					"invalid.deviceId", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		} catch(IndexOutOfBoundsException e){
			throw new MobileIdException(deviceId.toString(), null,
					"invalid.deviceId", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}
		return companyUserId;
	}
}
