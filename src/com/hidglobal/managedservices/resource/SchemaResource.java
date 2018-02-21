package com.hidglobal.managedservices.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;

/**
 * 
 * SchemaReource class provides methods to know about Mobile Access API Schemas.
 * 
 */
@Component
@Path("/{id}/schemas")
public class SchemaResource {
	private static final Logger logger = LoggerFactory
			.getLogger(SchemaResource.class);
	@Autowired
	private MAUtils maUtils;
	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	@Autowired
	private ErrorHelper errorHelper;

	@Resource(name = "userSchema")
	private ClassPathResource classPathUserResource;

	@Resource(name = "invitationSchema")
	private ClassPathResource classPathInvitationResource;

	@Resource(name = "deviceSchema")
	private ClassPathResource classPathDeviceResource;
	
	@Resource(name = "mobileIdSchema")
	private ClassPathResource classPathDeviceMobileIdResource;
	
	@Resource(name = "partNumberSchema")
	private ClassPathResource classPathPartNumberResource;
	
	@Resource(name = "userActionSchema")
	private ClassPathResource classPathUserActionSchemaResource;
	
	@Resource(name = "versionSchema")
	private ClassPathResource classPathVersionSchemaResource;
	
	@Resource(name = "registrationSchema")
	private ClassPathResource classPathRegistrationSchemaResource;
	
	@Resource(name = "notificationSchema")
	private ClassPathResource classPathNotificationSchemaResource;

	/**
	 * This method displays the User Schema of Mobile Access API.
	 * 
	 * @return User Schema in JSON format  
	 * @throws IOException 
	 */

	@GET
	@Path("/urn:ietf:params:scim:schemas:core:2.0:User")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getUserSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("User Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathUserResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing User Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.USERSCHEMALOCATION),new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}

	/**
	 * This method displays the Invitations Schema of Mobile Access API.
	 * 
	 * @return Invitation Schema in JSON format
	 * @throws IOException
	 */

	@GET
	@Path("/urn:hid:scim:api:ma:1.0:UserInvitation")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getInvitationSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("Invitation Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathInvitationResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing Invitation Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.INVITATIONSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}

	/**
	 * This method displays the Device Schema of Mobile Access API.
	 * 
	 * @return Device Schema in JSON format
	 * @throws IOException
	 */

	@GET
	@Path("/urn:hid:scim:api:ma:1.0:CredentialContainer")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getDeviceSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("Device Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathDeviceResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing Device Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.CREDENTIALCONTAINERSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}

	/**
	 * This method displays the DeviceMobileId Schema of Mobile Access API.
	 * 
	 * @return DeviceMobileId Schema in JSON format
	 * @throws IOException
	 */

	@GET
	@Path("/urn:hid:scim:api:ma:1.0:Credential")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getDeviceMobileIdSchema(
			@Context HttpServletRequest request, @PathParam("id") long companyId)
			throws IOException {
		logger.info("DeviceMobileIds Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathDeviceMobileIdResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing DeviceMobileIds Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.CREDENTIALSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}

	@GET
	@Path("/urn:hid:scim:api:ma:1.0:PartNumber")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getPartNumberSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("PartNumber Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathPartNumberResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing PartNumber Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.PARTNUMBERSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}

	@GET
	@Path("/urn:hid:scim:api:ma:1.0:UserAction")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getUserActionSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("UserAction Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathUserActionSchemaResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing UserAction Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.USERACTIONSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}
	
	@GET
	@Path("/urn:hid:scim:api:ma:1.0:SDKVersion")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getVersionSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("Version Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathVersionSchemaResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing Version Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.VERSIONSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}
	@GET
	@Path("/urn:hid:scim:api:ma:1.0:CallbackRegistration")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getRegistrationSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("Version Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathRegistrationSchemaResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing Version Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.REGISTRATIONSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}
	@GET
	@Path("/urn:hid:scim:api:ma:1.0:Notification")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getNotificationSchema(@Context HttpServletRequest request,
			@PathParam("id") long companyId) throws IOException {
		logger.info("Version Schema Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			is = classPathNotificationSchemaResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error(
					"Unexpect Error Happened while providing Version Schema Information: ",
					e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		}

		finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.NOTIFICATIONSCHEMALOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();

	}
}
