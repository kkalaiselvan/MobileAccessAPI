package com.hidglobal.managedservices.resource;

import java.text.MessageFormat;
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
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.business.RegisterationManagement;
import com.hidglobal.managedservices.exception.RegisterationException;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.CallBackUrlRequest;
import com.sun.jersey.api.client.ClientResponse.Status;

@Component
@Path("/{id}/callback-registration")
public class RegistrationResource {
	
	@Autowired
	private MAUtils maUtils;
	
	@Autowired
	private RegisterationManagement registerManagement;
	
	@Autowired
	private MAValidator validator;

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	
	@Autowired
	private ErrorHelper errorHelper;
	
	private static final Logger logger = LoggerFactory.getLogger(RegistrationResource.class);
	@POST
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response registerCallBackURL(@PathParam("id") long companyId,@Context HttpServletRequest request, CallBackUrlRequest registrationRequest)
	{
		logger.debug("Performing Operation: CallBAck Registration. Authenticated CompanyId: [{}]",
				companyId);

		long startTime = System.currentTimeMillis();
		
		CallBackUrlRequest callBackRegistrationResponse;
		try{
			
			validator.validateRegistrationSchema(registrationRequest);
			if((registrationRequest!=null)&&(registrationRequest.getCallBackURL().getUrl()!=null)){
				registrationRequest.getCallBackURL().setUrl(registrationRequest.getCallBackURL().getUrl().trim());
				}
			validator.checkRegistrationViolations(registrationRequest.getCallBackURL());
			callBackRegistrationResponse=registerManagement.doRegister(companyId, registrationRequest,maUtils.getRequestor(request));
		
		
		}catch (RegisterationException cie) {
			cie.printStackTrace();
			Response response = errorHelper.handleException(cie);
			return response;
		}
		 catch (Exception ex) {
			logger.error("Unexpected Error Happened while Registring Callback User: ", ex);
			return errorHelper.handleException(ex);
		}
		logger.debug("execution Time to Register Callback - [{}]",
				System.currentTimeMillis() - startTime);
		return Response.status(Status.CREATED).entity(callBackRegistrationResponse).header("location", callBackRegistrationResponse.getMeta().getLocation().toString()).build();
	}
	
	@DELETE
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Path("/{registrationId}")
	public Response unRegisterCallBackURL(@PathParam("id") long companyId,@PathParam("registrationId") long registrationId,
			@Context HttpServletRequest request)
	{
	 
	
		logger.debug("Performing Operation: CallBAck UnRegistration. Authenticated CompanyId: [{}]",
				companyId);

		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
	
		try{
		registerManagement.doUnRegister(companyId, registrationId,maUtils.getRequestor(request));
		
		}catch (RegisterationException cie) {
			cie.printStackTrace();
			Response response = errorHelper.handleException(cie);
			return response;
		}
		 catch (Exception ex) {
			logger.error("Unexpected Error Happened while unregistering callback url: ", ex);
			return errorHelper.handleException(ex);
		}
		logger.debug("execution Time to UnRegisteration Callback - [{}]",
				System.currentTimeMillis() - startTime);
		return Response.status(Status.NO_CONTENT).build();
	}
	
	
	@PUT
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Path("/{registrationId}")
	public Response updateCallBackURL(@PathParam("id") long companyId,
			@Context HttpServletRequest request,@PathParam("registrationId") long registrationId,CallBackUrlRequest registrationRequest)
	{
	 
	
		logger.debug("Performing Operation: CallBack Update Registration. Authenticated CompanyId: [{}]",
				companyId);

		long startTime = System.currentTimeMillis();
		
		CallBackUrlRequest callBackRegistrationResponse;

		String location;
		try{
			
			validator.validateRegistrationSchema(registrationRequest);
			if((registrationRequest!=null)&&(registrationRequest.getCallBackURL()!=null)){
				registrationRequest.getCallBackURL().setUrl(registrationRequest.getCallBackURL().getUrl().trim());
				registrationRequest.setId(registrationId);
			}
			validator.checkRegistrationViolations(registrationRequest.getCallBackURL());
			logger.info("URL to be updated [{}]",registrationRequest.getCallBackURL().getUrl());
			callBackRegistrationResponse=registerManagement.doUpdate(companyId,registrationRequest,maUtils.getRequestor(request));
			
			
			location = MessageFormat.format(
					resourceLocation.getProperty(HIDContants.CALLBACKLOCATION),
					new Long(companyId).toString()).concat("/"+registrationId);
		
			callBackRegistrationResponse.getMeta().setLocation(location);
		}catch (RegisterationException cie) {
			cie.printStackTrace();
			Response response = errorHelper.handleException(cie);
			return response;
		}
		 catch (Exception ex) {
			logger.error("Unexpected Error Happened while Updating callback URL: ", ex);
			return errorHelper.handleException(ex);
		}
		logger.debug("execution Time to Update Callback - [{}]",
				System.currentTimeMillis() - startTime);
		return Response.status(Status.OK).entity(callBackRegistrationResponse).header("location", location).build();
	}
	
	

	@GET
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Path("/{registrationId}")
	public Response getCallBackURL(@PathParam("id") long companyId,@PathParam("registrationId") long registrationId,
			@Context HttpServletRequest request)
	{
	 
	
		logger.debug("Performing Operation: Get RegistrationURL. Authenticated CompanyId: [{}]",
				companyId);

		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		CallBackUrlRequest callBackRegistrationResponse;
	
		try{
			callBackRegistrationResponse=registerManagement.doGet(companyId,registrationId);
			 
		
		}catch (RegisterationException cie) {
			cie.printStackTrace();
			Response response = errorHelper.handleException(cie);
			return response;
		}
		 catch (Exception ex) {
			logger.error("Unexpected Error Happened while getting registered callback URL: ", ex);
			return errorHelper.handleException(ex);
		}
		logger.debug("execution Time to Get Registered Callback - [{}]",
				System.currentTimeMillis() - startTime);
		return Response.status(Status.OK).header("Location", callBackRegistrationResponse.getMeta().getLocation()).entity(callBackRegistrationResponse).build();
	}
	
	
	/*
	@POST
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Path("/client")
	public Response testURL(@PathParam("id") long companyId,@Context HttpServletRequest request, Object object)
	{
		logger.debug("Test Operation: URL Invoked . Authenticated CompanyId: [{}]",
				companyId);
		logger.info("Request payload"+object.toString());

	
		return Response.status(Status.CREATED).build();
	}
	*/
}
