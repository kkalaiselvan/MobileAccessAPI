package com.hidglobal.managedservices.resource;

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
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.business.VersionManagement;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.vo.VersionResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

@Path("/{id}/sdk-version")
@Component
public class VersionResource {

	@Autowired
	private VersionManagement versionManagement;
	
	@Autowired
	private ErrorHelper errorHelper;
	
	private static final Logger logger = LoggerFactory.getLogger(VersionResource.class);

	@GET
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getVersion(@PathParam("id") long companyId,
			@Context HttpServletRequest request) {

		VersionResponse versionResponse = new VersionResponse();
		try {

			versionResponse = versionManagement.getVersion(companyId);
		} catch (Exception ex) {
			logger.error("Unexpected Error Happened while responding version: ", ex);
			return errorHelper.handleException(ex);
		}

		return Response.status(Status.OK).entity(versionResponse)	
				.header("location",versionResponse.getMeta().getLocation().toString()).build();
				
			
						
				

	}
}
