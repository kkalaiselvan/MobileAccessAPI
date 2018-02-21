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
import com.hidglobal.managedservices.business.DeviceManagement;
import com.hidglobal.managedservices.business.MobileIdsManagement;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchMobileIdResponse;
import com.hidglobal.managedservices.vo.UserDevices;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.exception.IssueCredentialException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.vo.UserMobileAction;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * DeviceResource Class provides interface to access Mobile Access Endpoints.
 * URI Path which contains {version}/devices would be processed by this class.
 * This class is also responsible for setting the Schema Information as per SCIM
 * Standards.
 * 
 */
@Component
@Path("/{id}/credential-container")
public class DeviceResource {
	@Autowired
	private MobileIdsManagement mobileIdsManagement;
	@Autowired
	private ErrorHelper errorHelper;
	@Autowired
	private DeviceManagement deviceManagement;
	@Autowired
	private MAUtils maUtils;
	@Autowired
	private MAValidator maValidator;
	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	private static final Logger logger = LoggerFactory
			.getLogger(DeviceResource.class);

	/**
	 * This method allows consumers to send a Mobile Id to the Device Endpoint.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param deviceId
	 *            The Device Endpoint to which Mobile Id to be issued
	 * @param userMobileActions
	 *            Additional information required to issue mobile id
	 * @return Returns HTTP Status Code 200 on success.
	 */

	@POST
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Path("/{deviceId}/credential")
	public Response issueMobileId(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("deviceId") long deviceId,
			UserMobileAction userMobileActions) {
		long startTime = System.currentTimeMillis();
		try {
			logger.debug("Authenticated Company ID [{}] ", companyId);
			maValidator.validateIssueCredential(userMobileActions);
			MobileId mobileId = mobileIdsManagement.issueMobileId(companyId,
					deviceId,  maUtils.getRequestor(request),
					userMobileActions);
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.MOBILE_ID.toString());
			mobileId.setSchemas(schemas);
			String location = MessageFormat.format(
					resourceLocation
							.getProperty(HIDContants.MOBILEIDLOCATION)
							.concat("/").concat(mobileId.getId().toString()), new Long(companyId).toString());
			logger.debug("execution Time to Update user - [{}] ms",
					System.currentTimeMillis() - startTime);
			return Response.status(Status.OK).entity(mobileId).header("Location", location).build();
		} catch (IssueCredentialException dbe) {
			dbe.printStackTrace();
			logger.error("Unexpect Error Happened while Isssuing credential: ", dbe.getMessage());
			Response response = errorHelper.handleException(dbe);
			return response;
		}catch (MAValidationException ve) {
			Response response = errorHelper.handleException(ve);
			return response;
		} 
		
		catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Unexpect Error Happened while Issuing credential: ", ex);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		} 
		

	}

	/**
	 * This method allows consumers to terminate/ Inactivate a Device Endpoint.
	 * 
	 * @param companyId
	 *            The Authenticated Company ID
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param deviceId
	 *            The device endpoint id whose endpoint is to be
	 *            terminated/deleted.
	 * @return Returns HTTP Status Code 200 ok success.
	 */

	@DELETE
	@Path("/{deviceId}/")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response deleteDevice(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("deviceId") Long deviceId) {
		long startTime = System.currentTimeMillis();

		try {
			logger.debug("Authenticated Company ID [{}] ", companyId);
			maUtils.getRequestor(request);
			deviceManagement.deleteDevice(deviceId, companyId);

			logger.debug("execution Time to Update user - [{}] ms",
					System.currentTimeMillis() - startTime);
			return Response.status(Status.NO_CONTENT).build();
		} catch (MobileIdException e) {
			e.printStackTrace();
			logger.error("Unexpect Error Happened while Enrolling User: ", e);
			Response response = errorHelper.handleException(e);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

	}

	/**
	 * This method allows consumers to fetch the current status of device
	 * information. Any mobile ids associated with Device will also be fetched.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param deviceId
	 *            The device endpoint id whose information needs to be fetched.
	 * @return Returns the device information and associated mobile id
	 *         information.
	 */

	@GET
	@Path("/{deviceId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getDeviceDetails(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("deviceId") long deviceId) {
		logger.debug(" Authenticated Company ID is [{}]", companyId);

		logger.debug("Fetching Device Details for Device ID [{}]", deviceId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setDeviceId(deviceId);
			Device device = deviceManagement.getDeviceDetails(filterByIds);
			device.getMeta().setLocation(
					MessageFormat.format(device.getMeta().getLocation(),
							new Long(companyId).toString()));
			UserDevices devices = new UserDevices();
			List<Device> d = new ArrayList<Device>();
			d.add(device);
			devices.setDevices(d);
			/**
			 * Sets Schema debugrmation
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.DEVICES.toString());
			devices.setSchemas(schemas);
			for (Device d1 : d) {
				if ((d1.getMobileIds() != null)
						&& (d1.getMobileIds().size() > 0)) {
					schemas.add(Schemas.MOBILE_ID.toString());
					break;
				}
			}

			logger.debug("execution Time to Get Device - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(HIDContants.SUCCESS).entity(devices).header("Location", device.getMeta().getLocation()).build();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpect Error Happened while Get Device Details:{[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method retrieves the meta information of all mobile ids associated
	 * with a device endpoint.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param deviceId
	 *            The device endpoint id whose information is retrieved.
	 * @return Returns the meta information about Mobile Ids associated with the
	 *         device.
	 */

	@GET
	@Path("/{deviceId}/credential")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response listMobileIds(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("deviceId") long deviceId) {
		logger.debug("Authenticated Company ID [{}] ", companyId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setDeviceId(deviceId);
			SearchMobileIdResponse mobileIdResponse = mobileIdsManagement
					.listDeviceMobileIds(filterByIds);
			
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			mobileIdResponse.setSchemas(schemas);
			String location = MessageFormat.format(
					resourceLocation.getProperty(HIDContants.DEVICELOCATION),
					new Long(companyId).toString()).concat("/").concat(String.valueOf(deviceId)).concat("/credential");
			/**
			 * Set Meta Information
			 */

			logger.debug("execution Time to List Mobile Ids - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(HIDContants.SUCCESS)
					.entity(mobileIdResponse).header("Location",location ).build();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpect Error Happened while Listing Mobile Ids:{[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}

}
