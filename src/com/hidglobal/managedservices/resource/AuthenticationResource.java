package com.hidglobal.managedservices.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.ErrorDetails;
import com.hidglobal.managedservices.vo.Schema;

/*
 * Create a Response for an unauthenticated request.
 */
@Component
@Path("/authentication")
public class AuthenticationResource {
	
	@Autowired
	private ErrorHelper errorHelper;
	
	public AuthenticationResource() {
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response unauthenticated() {
		Response response = null;
		try {
			//TODO Auditing

			ErrorDetails errorDetails = new ErrorDetails();

			String detail = "Unauthorized -- Mobile Access API";
			String scimType = Schema.Schemas.ERROR.toString();
			errorDetails.setStatus(HIDContants.UNAUTHORIZED);
			errorDetails.setDetail(detail);
			
			errorDetails.setScimType(scimType);
			ResponseBuilder respBuilder = Response.status(Response.Status.UNAUTHORIZED);
			
			//  WWW-Authenticate  = "WWW-Authenticate" ":" 1#challenge
			respBuilder.header("WWW-Authenticate",	"Basic realm=\"weblogic\"");
			
			//response = Response.status(Response.Status.UNAUTHORIZED).entity(errorDetails).build();
			
			response = respBuilder.entity(errorDetails).build();
		}
		catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			System.out.println(" Exception - COntroller");
			response = errorHelper
					.setErrorResponse("Unable to Process Request. Please try again later or Contact Admin");
		}finally{
			return response;
		}
	}




}
