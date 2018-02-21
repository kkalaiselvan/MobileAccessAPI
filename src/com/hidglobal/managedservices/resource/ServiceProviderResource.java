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
 * ServiceProviderResource class provides methods to know about Mobile Access API service configurations.
 *
 */
@Component
@Path("/{id}/serviceProviderConfig")
public class ServiceProviderResource {
	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderResource.class);
	
	@Resource(name="serviceProviderConfigResource")
	private ClassPathResource classPathResource;
	@Autowired
	private ErrorHelper errorHelper;
	@Autowired
	private MAUtils maUtils;
	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	/**
	 * This method displays the service provider configurations of Mobile Access API.
	 * @return
	 * @throws IOException
	 */
	@GET
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getServiceProviderConfig(@Context HttpServletRequest request, @PathParam("id") long companyId) throws IOException {
		logger.info("ServiceProviderConfig Requested");
		char[] c = new char[50000];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			maUtils.getRequestor(request);
			// InputStream is =
			// ServiceProviderResource.class.getResourceAsStream("/ServiceConfig.json");
			is = classPathResource.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			len = br.read(c);
			if (len > -1) {
				sb.append(c, 0, len);
			}
		} catch (Exception e) {
			logger.error("Unexpected Error Happened while providing service config Information: ", e);
			Response response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
			return response;
		} finally {
			// releases resources associated with the streams
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		}
		
		String location=MessageFormat.format(resourceLocation.getProperty(HIDContants.SERVICECONFIGLOCATION), new Long(companyId).toString());
		return Response.ok(sb.toString()).header("Location", location).build();
	}
}
