package com.hidglobal.managedservices.resource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.business.DeviceManagement;
import com.hidglobal.managedservices.business.IInvitationService;
import com.hidglobal.managedservices.business.IUserService;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.exception.UserException;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.PacsInvitation;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchDevicesResponse;
import com.hidglobal.managedservices.vo.SearchInvitationResponse;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.SearchUserResponse;
import com.hidglobal.managedservices.vo.UserInvitations;

/**
 * 
 * UserResource Class provides interface to access PACS Users of Mobile Access.
 * URI Path which contains {version}/users would be processed by this class.
 * This class is also responsible for setting the Schema Information as per SCIM
 * Standards.
 * 
 */

@Component
@Path("/{id}/users")
public class UserResource {

	@Autowired
	IUserService userService;
	@Autowired
	private DeviceManagement deviceManagement;
	@Autowired
	IInvitationService invitationService;
	@Autowired
	private ErrorHelper errorHelper;
	@Autowired
	private MAValidator maValidator;

	@Autowired
	private MAUtils maUtils;

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;

	private final static Logger logger = LoggerFactory
			.getLogger(UserResource.class);

	/**
	 * This method allows consumers to enroll single PACS User with possible
	 * User Actions.
	 * 
	 * Possible User Actions are: 1. Create Invitation 2. Auto Assign Mobile Id
	 * 3. send Invitation
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param user
	 *            PACS User Object
	 * @return Returns Created PACS User information in JSON format
	 * 
	 */

	@POST
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response createUser(@Context HttpServletRequest request,
			@PathParam("id") long companyId, PacsUser user) {

		try {
			logger.debug(
					"Performing Operation: Enroll User. Authenticated Company {0}",
					companyId);
			long startTime = System.currentTimeMillis();
			user.setCompanyId(String.valueOf(companyId));
			user.setUserName(maUtils.getRequestor(request));
			maValidator.validateUserSchema(user);
			maValidator.validateUserforInsert(user);
			user = userService.enrollUser(user);
			user.setSchemas(null);
			if (user.getUserInvitations() != null) {
				user.getUserInvitations().get(0).setMeta(null);
			}
			List<String> schemaList = new ArrayList<String>();
			schemaList.add(Schemas.PACSUSER.toString());
			if (user.getUserInvitations() != null) {
				schemaList.add(Schemas.INVITATIONS.toString());
			}
			if (user.getUsermobileIds() != null) {
				schemaList.add(Schemas.MOBILE_ID.toString());
			}
			user.setSchemas(schemaList);
			user.getMeta().setLocation(
					MessageFormat.format(user.getMeta().getLocation(),
							new Long(companyId).toString()));
			logger.debug("execution Time to enroll user - [{}]",
					System.currentTimeMillis() - startTime);

			return Response.status(Response.Status.CREATED).entity(user)
					.header("Location", user.getMeta().getLocation()).build();
		}catch (MobileIdException me) {
			Response response = errorHelper.handleException(me);
			return response;
		}

		catch (MAValidationException ve) {
			Response response = errorHelper.handleException(ve);
			return response;
		}

		catch (DBException dbe) {
			Response response = errorHelper.handleException(dbe);
			return response;
		}

		catch (MobileAccessException mae) {
			Response response = errorHelper.handleException(mae);
			return response;
		} catch (InvitationException cie) {
			cie.printStackTrace();
			Response response = errorHelper.handleException(cie);
			return response;
		} catch (Exception ex) {
			logger.error("Unexpected Error Happened while Enrolling User: ", ex);
			return errorHelper.handleException(ex);
		}

	}

	/**
	 * 
	 * This method allows the consumer to update single PACS User details.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param user
	 *            PACS User Object
	 * @param companyUserId
	 *            The Company User ID whose information needs to be updated.
	 * @return Returns Updated PACS User Details in JSON Format
	 */

	@PUT
	@Path("{companyUserId}")
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response updateUser(@Context HttpServletRequest request,
			@PathParam("id") long companyId, PacsUser user,
			@PathParam("companyUserId") long companyUserId) {

		try {

			logger.debug(
					"Performing Operation: Update User. Authenticated CompanyId: [{}]. Company User Id: [{}]",
					companyId, companyUserId);

			long startTime = System.currentTimeMillis();
			user.setCompanyId(String.valueOf(companyId));
			user.setUserName(maUtils.getRequestor(request));
			user.setId(companyUserId);
			maValidator.validateUserforUpdate(user);
			userService.updateUser(user);

			/**
			 * Sets Schema Information
			 */

			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.PACSUSER.toString());
			user.setSchemas(schemas);
			user.getMeta().setLocation(
					MessageFormat.format(user.getMeta().getLocation(),
							new Long(companyId).toString()));
			logger.debug("execution Time to Update user - [{}] ms",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK).entity(user)
					.header("Location", user.getMeta().getLocation()).build();
		}

		catch (MAValidationException ve) {
			Response response = errorHelper.handleException(ve);
			return response;
		} catch (DBException dbe) {
			Response response = errorHelper.handleException(dbe);
			return response;
		}

		catch (MobileAccessException mae) {
			Response response = errorHelper.handleException(mae);
			return response;
		}

		catch (Exception ex) {
			logger.error(
					"Unexpected Error Happened while Updating User:{[]}, {[]} ",
					companyUserId, ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * Gets the PACS User Details for the requested CompanyUserId.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            The Company User ID whose information needs to be fetched.
	 *            Inventory Details are: 1.PACS User Information 2. Invitation
	 *            Details, if any. 3. Device Details, if any 4. MobileIds for
	 *            each Device, if any.
	 * @return Returns requested PACS User information in JSON format
	 */

	@GET
	@Path("{companyUserId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getUserDetails(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("companyUserId") long companyUserId) {

		try {
			logger.debug(
					"Performing Operation: Get User. Authenticated CompanyId: [{}]. Company User Id: [{}]",
					companyId, companyUserId);
			long startTime = System.currentTimeMillis();
			PacsUser user = new PacsUser();
			user.setCompanyId(String.valueOf(companyId));
			user.setUserName(maUtils.getRequestor(request));
			user.setId(companyUserId);
			user = userService.getUserDetails(user);

			/**
			 * Sets Schema Information
			 */

			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.PACSUSER.toString());
			if (user.getUserInvitations() != null)
				schemas.add(Schemas.INVITATIONS.toString());

			if ((user.getUserDevices() != null)
					&& (user.getUserDevices().size() > 0)) {
				schemas.add(Schemas.DEVICES.toString());

				for (Device device : user.getUserDevices()) {
					if ((device.getMobileIds() != null)
							&& (device.getMobileIds().size() > 0)) {
						schemas.add(Schemas.MOBILE_ID.toString());
						break;
					}
				}

			}
			user.setSchemas(schemas);
			user.getMeta().setLocation(
					MessageFormat.format(user.getMeta().getLocation(),
							new Long(companyId).toString()));
			logger.debug("execution Time to Get user details- [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK).entity(user)
					.header("Location", user.getMeta().getLocation()).build();
		} catch (DBException dbe) {
			logger.error("User [{}] Not Found", dbe.getErrorData());
			Response response = errorHelper.handleException(dbe);
			return response;
		} catch (Exception ex) {

			logger.error(
					"Unexpected Error Happened while Fetching User Details:{[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}

	/**
	 * This method allows the consumer to retrieve the recently created/modified
	 * 25 PACS Users Meta Information for the requested company ID.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * 
	 * @return Returns the recently created/modified PACS Users Meta Information
	 *         in JSON Format.
	 */
	@GET
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response listUsers(@Context HttpServletRequest request,
			@PathParam("id") long companyId) {

		try {
			logger.debug(
					"Performing Operation: List User. Authenticated CompanyId: [{}].",
					companyId);
			long startTime = System.currentTimeMillis();
			// maUtils.getRequestor(request);
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			SearchUserResponse userList = userService.listUsers(filterByIds);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			userList.setSchemas(schemas);
			String location = MessageFormat.format(
					resourceLocation.getProperty(HIDContants.USERLOCATION),
					new Long(companyId).toString());

			logger.debug("execution Time to List user - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK).entity(userList)
					.header("Location", location).build();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpected Error Happened while Listing User Details:{[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method allows consumer to perform search operation on PACS User.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param searchRequest
	 *            SearchRequest object that has search attributes, search
	 *            filters, count requested by consumer in SCIM Format.
	 * @return Returns the list of Users with queried attributes and meta
	 *         information in SCIM format.
	 * @throws MAValidationException 
	 */
	@POST
	@Path("/.search")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response searchUsers(@Context HttpServletRequest request,
			@PathParam("id") long companyId, SearchRequest searchRequest) {

		try {
			logger.debug(
					"Performing Operation: Search User. Authenticated CompanyId: [{}].",
					companyId);
			long startTime = System.currentTimeMillis();
			maUtils.getRequestor(request);
			SearchUserResponse userList;

			/**
			 * Sets Schema Information
			 */
			  maValidator.validateSearchUserAttribute(searchRequest);
			maValidator.validateSearchRequest(searchRequest, Schemas.PACSUSER);
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			userList = userService.searchUsers(searchRequest, filterByIds);
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			userList.setSchemas(schemas);
			String location = MessageFormat.format(resourceLocation
					.getProperty(HIDContants.USERLOCATION).concat("/.search"),
					new Long(companyId).toString());
			logger.debug("execution Time to Search user - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK).entity(userList)
					.header("Location", location).build();

		} catch (MAValidationException ve) {
			System.out.println(ve.getErrorData());
			Response response = errorHelper.handleException(ve);
			return response;
		} 
		catch(DataIntegrityViolationException dive){
			String errorMsg = dive.getCause().getMessage();			
			System.out.println(errorMsg);
			Response response = errorHelper.handleException(new MAValidationException("","Exception while parsing filter," +
					""+errorMsg.substring(errorMsg.indexOf(':')+1, 
					 errorMsg.length()).trim()+".","", 
					 HIDContants.INVALIDFILTER,HIDContants.BADREQUEST));
			return response;		
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpected Error Happened while Searching User: {[]} ", ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This Operation is invoked when the consumer wants to delete a PACS User.
	 * The method cancels any pending invitations associated with the User,
	 * Revokes the issued credential and deleted the Device Endpoint, if any,
	 * before Deleting the PACS User.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            The Company User ID to be deleted
	 * @return Returns HTTP 200 Status in Header.
	 */

	@DELETE
	@Path("/{companyUserId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response deleteuser(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("companyUserId") long companyUserId) {

		try {
			logger.debug(
					"Performing Operation: Delete User. Authenticated CompanyId: [{}]. Company User Id: [{}]",
					companyId, companyUserId);
			long startTime = System.currentTimeMillis();
			maUtils.getRequestor(request);
			userService.deleteUser(companyUserId, String.valueOf(companyId));
			logger.debug("execution Time for Delete user - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (UserException e) {
			Response response = errorHelper.handleException(e);
			return response;

		}

		catch (MobileAccessException e) {
			Response response = errorHelper.handleException(e);
			return response;

		} catch (Exception e) {
			logger.error("Unexpected Error Happened while Deleting User:[{}] ",
					e);
			Response response = errorHelper.handleException(e);
			return response;

		}

	}

	/**
	 * Gets the Invitation Details for the requested CompanyUserId.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            The Company User ID whose Invitation Details to be fetched.
	 * @return Returns Invitation information of requested PACS User in JSON
	 *         format
	 */

	@GET
	@Path("{companyUserId}/invitation/{invitationId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getInvitation(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("companyUserId") long companyUserId,
			@PathParam("invitationId") long invitationId) {

		try {
			logger.debug(
					"Performing Operation: Get Invitation. Authenticated CompanyId: [{}]. Invitation ID: [{}]",
					companyId, invitationId);

			long startTime = System.currentTimeMillis();
			maUtils.getRequestor(request);
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setCompanyUserId(companyUserId);
			filterByIds.setInvitationId(invitationId);

			Invitation invitation = invitationService
					.getInvitationDetails(filterByIds);
			/**
			 * Sets Schema Information
			 */

			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.INVITATIONS.toString());
			invitation.setSchemas(schemas);

			/**
			 * Set Meta Information
			 */

			invitation.getMeta().setResourceType(
					MessageFormat.format(invitation.getMeta().getLocation(),
							new Long(companyId).toString()));
			logger.debug("execution Time to Get Invitation - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(HIDContants.SUCCESS).entity(invitation)
					.header("Location", invitation.getMeta().getLocation())
					.build();
		} catch (DBException dbe) {
			logger.error("User [{}] Not Found", dbe.getErrorData());
			Response response = errorHelper.handleException(dbe);
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpect Error Happened while Searching User: {[]}, {[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}

	/**
	 * This method allows consumer to perform search operation on Invitation.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            The Company User Id whose Invitation details to be searched.
	 * @param searchRequest
	 *            SearchRequest object that has search attributes, search
	 *            filters, count requested by consumer in SCIM Format.
	 * @return Returns the list of Invitations with queried attributes and meta
	 *         information in SCIM format.
	 */

	@POST
	@Path("{companyUserId}/invitation/.search")
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response searchInvitations(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("companyUserId") long companyUserId,
			SearchRequest searchRequest) {

		try {

			logger.debug(
					"Performing Operation: Search Invitation. Authenticated CompanyId: [{}].",
					companyId);

			long startTime = System.currentTimeMillis();
			maUtils.getRequestor(request);
			SearchInvitationResponse invitationResponse;
			/**
			 * Sets Schema Information
			 */
			maValidator.validateSearchRequest(searchRequest,
					Schemas.INVITATIONS);
           maValidator.validateSearchInvitationAttribute(searchRequest);
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setCompanyUserId(companyUserId);
			invitationResponse = invitationService.searchInvitation(
					searchRequest, filterByIds);
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			invitationResponse.setSchemas(schemas);

			/**
			 * Set Meta Information
			 */
			String location = MessageFormat.format(
					resourceLocation.getProperty(HIDContants.USERLOCATION)
							.concat("/" ).concat(String.valueOf(companyUserId))
							.concat("/invitation/.search"), new Long(companyId).toString());
			logger.debug("execution Time to Search Invitation - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK)
					.entity(invitationResponse).header("Location", location)
					.build();
		} catch (MAValidationException ve) {
			logger.error(
					"Unexpected Error Happened while Searching Invitation for Company User Id: {[]} ",
					ve);
			Response response = errorHelper.handleException(ve);
			return response;
		}
		catch(DataIntegrityViolationException dive){
			String errorMsg = dive.getCause().getMessage();			
			System.out.println(errorMsg);
			Response response = errorHelper.handleException(new MAValidationException("","Exception while parsing filter," +
					""+errorMsg.substring(errorMsg.indexOf(':')+1, 
					 errorMsg.length()).trim()+".","", 
					 HIDContants.INVALIDFILTER,HIDContants.BADREQUEST));
			return response;		
		}
		catch (Exception ex) {
			logger.error(
					"Unexpected Error Happened while Searching Invitation for Company User Id: {[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method allows consumer to perform Create Invitation operation on
	 * Invitation with possible User Actions.
	 * 
	 * Possible User Actions are: 1. Auto Assign Mobile Id 2. send Invitation.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            The Company User Id for which the Invitation needs to be
	 *            created
	 * @param pacsInvitation
	 *            pacsInvitation object that has possible user actions
	 * @return Returns the Invitation Information JSON format.
	 */

	@POST
	@Path("/{compnayUserId}/invitation")
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response createInvitation(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("compnayUserId") long compnayUserId,
			PacsInvitation pacsInvitation) throws MAValidationException,
			MobileAccessException {

		try {

			logger.debug(
					"Performing Operation: Create Invitation. Authenticated CompanyId: [{}].",
					companyId);
			long startTime = System.currentTimeMillis();

			UserInvitations userInvitations = new UserInvitations();
			PacsUser pacsUser;
			maValidator.validateInvitationSchema(pacsInvitation);
			pacsUser = invitationService.createInvitation(compnayUserId,
					pacsInvitation.getUserActions(), String.valueOf(companyId),
					maUtils.getRequestor(request));
			List<String> schemaList = new ArrayList<String>();
			schemaList.addAll(pacsUser.getSchemas());
			List<Invitation> invitationList = new ArrayList<Invitation>();
			invitationList.add(pacsUser.getUserInvitations().get(0));
			userInvitations.setInvitations(invitationList);
			if (pacsUser.getUsermobileIds() != null) {
				List<MobileId> mobileIdList = new ArrayList<MobileId>();
				mobileIdList.add(pacsUser.getUsermobileIds().get(0));
				userInvitations.setMobileId(mobileIdList);
			}
			userInvitations.setSchemas(schemaList);
			logger.debug("execution Time for create Invitation - [{}]",
					System.currentTimeMillis() - startTime);
			return Response
					.status(Response.Status.CREATED)
					.entity(userInvitations)
					.header("Location",
							pacsUser.getUserInvitations().get(0).getMeta()
									.getLocation()).build();
		}catch (MobileIdException me) {
			Response response = errorHelper.handleException(me);
			return response;
		}catch (InvitationException e) {
			Response response = errorHelper.handleException(e);
			return response;
		} catch (MAValidationException e) {
			Response response = errorHelper.handleException(e);
			return response;
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while Creating Invitation:[{}] ",
					e);
			Response response = errorHelper.handleException(e);
			return response;
		}

	}

	/**
	 * This method allows the consumer to retrieve the recently created/modified
	 * 25 PACS Invitation Meta Information for the requested companyuserid.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            THe Company User Id for which the Invitations to be listed
	 * @return Returns the recently created/modified Invitations Meta
	 *         Information of requested company user id in JSON Format.
	 */

	@GET
	@Path("/{companyUserId}/invitation")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response listInvitations(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("companyUserId") Long companyUserId) {
		try {

			logger.debug(
					"Performing Operation: List Invitation. Authenticated CompanyId: [{}]. Company User ID: [{}]",
					companyId, companyUserId);
			long startTime = System.currentTimeMillis();
			maUtils.getRequestor(request);
			QueryFilterId queryFilterId = new QueryFilterId();
			queryFilterId.setCompanyId(companyId);
			queryFilterId.setCompanyUserId(companyUserId);
			SearchInvitationResponse invitationResponse = invitationService
					.listInvitation(queryFilterId);

			// Sets Schema Information

			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			invitationResponse.setSchemas(schemas);
			String location = MessageFormat.format(resourceLocation
					.getProperty(HIDContants.USERLOCATION), new Long(companyId).toString()).concat("/").concat(companyUserId.toString().concat("/invitation"));
			logger.debug("execution Time for List Invitation - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK)
					.entity(invitationResponse).header("Location", location)
					.build();
		} catch (Exception ex) {

			logger.error(
					"Unexpected Error Happened while Listing Invitation :  {[]}",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}

	/**
	 * This method allows the consumer to retrieve the recently created/modified
	 * 25 Devices Meta Information for the requested companyuserid.
	 * 
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param companyUserId
	 *            THe Company User Id for which the Devices to be listed
	 * @return Returns the recently created/modified Invitations Meta
	 *         Information of Devices for requested company user id in JSON
	 *         Format.
	 */

	@GET
	@Path("/{companyUserId}/credential-container")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response listDevices(@Context HttpServletRequest request,
			@PathParam("id") long companyId,
			@PathParam("companyUserId") Long companyUserId) {
		try {

			logger.debug(
					"Performing Operation: List Devices. Authenticated CompanyId: [{}]. Company User ID: [{}]",
					companyId, companyUserId);
			long startTime = System.currentTimeMillis();
			maUtils.getRequestor(request);
			QueryFilterId queryFilterId = new QueryFilterId();
			queryFilterId.setCompanyId(companyId);
			queryFilterId.setCompanyUserId(companyUserId);
			SearchDevicesResponse deviceResponse = deviceManagement
					.listDevices(queryFilterId);

			// Sets Schema Information

			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			deviceResponse.setSchemas(schemas);
			String location = MessageFormat.format(
					resourceLocation.getProperty(HIDContants.USERLOCATION).concat("/").concat(companyUserId.toString().concat("/credential-container")),
					new Long(companyId).toString());
			logger.debug("execution Time for List Devices - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(HIDContants.SUCCESS).entity(deviceResponse)
					.header("Location", location).build();

		} catch (Exception ex) {

			logger.error(
					"Unexpected Error Happened while Listing Device: Company ID {[]}",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}
}
