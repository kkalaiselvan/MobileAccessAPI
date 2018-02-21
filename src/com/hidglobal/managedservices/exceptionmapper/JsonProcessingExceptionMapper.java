package com.hidglobal.managedservices.exceptionmapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;

@Component
@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
	@Autowired
	ErrorHelper errorHelper;
	@Override
	public Response toResponse(JsonProcessingException e) {
		String fieldName = null;
		Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(e.getMessage());
		while (matcher.find()){
		    fieldName = matcher.group(1);
		}
				return errorHelper.setErrorResponse("invalid.json", 
				null,
				fieldName,
				HIDContants.INVALIDSYNTAX, 
				Response.Status.BAD_REQUEST.getStatusCode());
	}

}
