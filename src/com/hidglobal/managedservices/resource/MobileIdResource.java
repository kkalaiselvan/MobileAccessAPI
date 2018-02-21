package com.hidglobal.managedservices.resource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceException;
import com.hidglobal.managedservices.business.MobileIdsManagement;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.MobileIds;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * MobileIdResource Class provides interface to operate and manage Mobile Ids.
 * URI Path which contains {version}/mobileids would be processed by this class.
 * This class is also responsible for setting the Schema Information as per SCIM
 * Standards.
 * 
 */

@Component
@Path("/{id}/credential")
public class MobileIdResource {
	@Autowired
	private ErrorHelper errorHelper;

	@Autowired
	private MobileIdsManagement mobileIdsManagement;

	@Autowired
	private MAUtils maUtils;

	private static final Logger logger = LoggerFactory.getLogger(MobileIdResource.class);

	/**
	 * This method allow consumers to fetch the requested credential
	 * information. Consumers can fetch credential details other than those
	 * which are in state "CARD ALLOCATED"
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param credentialId
	 *            The credential id whose information needs to be retrieved.
	 * @return
	 */

	@GET
	@Path("/{credentialId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getMobileIdDetails(@PathParam("id") long companyId, @Context HttpServletRequest request,
			@PathParam("credentialId") long credentialId) {
		logger.info(" Authenticated Company ID is [{}]", companyId);

		logger.info("Fetching MobileId Details for Mobile ID [{}]", credentialId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setMobileId(credentialId);
			MobileId mobileId = mobileIdsManagement.getMobileIdDetails(filterByIds);
			mobileId.getMeta().setLocation(MessageFormat.format(mobileId.getMeta().getLocation(), new Long(companyId).toString()));
			MobileIds mobileIds = new MobileIds();
			List<MobileId> m = new ArrayList<MobileId>();
			m.add(mobileId);
			mobileIds.setMobileIds(m);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.MOBILE_ID.toString());
			mobileIds.setSchemas(schemas);
			logger.info("execution Time to Get MobileId - [{}]", System.currentTimeMillis() - startTime);
			return Response.status(HIDContants.SUCCESS).entity(mobileIds).header("Location", mobileId.getMeta().getLocation()).build();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Unexpected Error Happened while Get MobileId Details:{[]} ", ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method allows consumers to revoke a mobile Id from a device
	 * endpoint. Credential in status ISSUED, ISSUING, ISSUE_INITIATED can be
	 * revoked.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param mobileId
	 *            The mobileId which needs to revoked
	 * @return Returns HTTP status code 200 on success.
	 * 
	 * @throws MobileAccessException
	 */
	@DELETE
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Path("{mobileId}")
	public Response revokeMobileId(@PathParam("id") long companyId, @Context HttpServletRequest request,
			@PathParam("mobileId") long mobileId) throws WebServiceException, MAValidationException,
					javax.xml.ws.WebServiceException, MobileAccessException {

		long startTime = System.currentTimeMillis();
		logger.info("Authenticated Company ID [{}] ", companyId);
		maUtils.getRequestor(request);
		try {

			mobileIdsManagement.deleteMobileId(companyId, mobileId);
			logger.info("execution Time for Delete mobileIds - [{}]", System.currentTimeMillis() - startTime);
		} catch (MobileAccessException e) {
			Response response = errorHelper.handleException(e);
			return response;

		} catch (MobileIdException e) {
			Response response = errorHelper.handleException(e);
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			Response response = errorHelper.handleException(e);
			return response;

		}
		return Response.status(Status.NO_CONTENT).build();
		

	}
}
