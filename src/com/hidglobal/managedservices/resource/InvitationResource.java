package com.hidglobal.managedservices.resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.business.IInvitationService;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.UserInvitations;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * InvitationResource Class provides interface to perform operations on
 * Invitation. URI Path which contains {version}/invitations would be processed
 * by this class. This class is also responsible for setting the Schema
 * Information as per SCIM Standards.
 * 
 */
@Component
@Path("/{id}/invitation")
public class InvitationResource {
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
	private static final Logger logger = LoggerFactory.getLogger(InvitationResource.class);

	/**
	 * This method allows consumers to fetch invitation details.
	 * 
	 * @param companyId
	 *            The Authenticated Company ID
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param invitationId
	 *            The invitation id for which the details to be retrieved.
	 * @return Returns the invitation details
	 */
	@GET
	@Path("/{invitationId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getInvitation(@PathParam("id") long companyId, @Context HttpServletRequest request,
			@PathParam("invitationId") long invitationId) {

		logger.debug("Performing Operation: Get Invitation. Authenticated CompanyId: [{}]. Invitation ID: [{}]",
				companyId, invitationId);

		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setInvitationId(invitationId);
			Invitation invitation = invitationService.getInvitationDetails(filterByIds);
			invitation.getMeta().setLocation(MessageFormat.format(invitation.getMeta().getLocation(), new Long(companyId).toString()));
			UserInvitations invitations = new UserInvitations();
			List<Invitation> invitationList = new ArrayList<Invitation>();
			invitationList.add(invitation);
			invitations.setInvitations(invitationList);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.INVITATIONS.toString());
			invitations.setSchemas(schemas);
			logger.debug("execution Time to Get Invitation - [{}]", System.currentTimeMillis() - startTime);
			return Response.status(Status.OK).entity(invitations).header("Location",invitation.getMeta().getLocation() ).build();
		} catch (DBException dbe) {
			logger.error("Invitation [{}] Not Found", dbe.getErrorData());
			Response response = errorHelper.handleException(dbe);
			return response;
		} catch (Exception ex) {

			logger.error("Unexpected Error Happened while Fetching Invitation Details: {[]} ", ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}

	/**
	 * This method allows consumers to notify the company user about the
	 * generated invitation id. The e-mail id contains the Invitation Code that
	 * needs to be registered with the device.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param invitationId
	 *            The invitation id, for which the corresponding invitation code
	 *            needs to be e-mailed.
	 * @return Returns HTTP Status code 200 on success.
	 */

	@POST
	@Path("/{invitationId}/email")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response sendInvitation(@PathParam("id") long companyId, @Context HttpServletRequest request,
			@PathParam("invitationId") Long invitationId) throws MobileAccessException, Exception {

		logger.debug("Performing Operation: Send Invitation. Authenticated CompanyId: [{}]. Invitation ID: [{}]",
				companyId, invitationId);

		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			invitationService.fetchSendInvitationDetails(String.valueOf(companyId), invitationId);
			logger.debug("execution Time for Send Invitation - [{}]", System.currentTimeMillis() - startTime);
			String location=MessageFormat.format(resourceLocation
					.getProperty(HIDContants.INVITATIONLOCATION).concat("/").concat(invitationId.toString().concat("/email")),
					new Long(companyId).toString());
			return Response.status(Status.OK).header("Location", location).build();
		} catch (InvitationException ie) {
			Response response = errorHelper.handleException(ie);
			return response;

		} catch (MAValidationException e) {
			Response response = errorHelper.handleException(e);
			return response;
		} catch (Exception e) {
			logger.error("Unexpected Error Happened while Sending Invitation:[{}] ", e);
			Response response = errorHelper.handleException(e);
			return response;
		}

	}

	/**
	 * This method allows consumers to cancel the invitation that has been
	 * generated by create invitation method.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param invitationId
	 *            The invitation id which needs to be cancelled.
	 * @return
	 */
	@DELETE
	@Path("/{invitationId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response cancelInvitation(@PathParam("id") long companyId, @Context HttpServletRequest request,
			@PathParam("invitationId") long invitationId) {

		logger.debug("Performing Operation: Cancel Invitation. Authenticated CompanyId: [{}]. Invitation ID: [{}]",
				companyId, invitationId);

		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {

			logger.debug("Authenticated Company ID [{}] ", companyId);

			invitationService.cancelInvitation(invitationId, String.valueOf(companyId));
			logger.debug("execution Time for cancel Invitation - [{}]", System.currentTimeMillis() - startTime);
			return Response.status(Status.NO_CONTENT).build();
		} catch (InvitationException ie) {
			Response response = errorHelper.handleException(ie);
			return response;

		} catch (Exception e) {
			logger.error("Unexpected Error Happened while Cancelling Invitation:[{}] ", e);
			Response response = errorHelper.handleException(e);
			return response;
		}

	}

	public MAValidator getMaValidator() {
		return maValidator;
	}

	public void setMaValidator(MAValidator maValidator) {
		this.maValidator = maValidator;
	}

}
