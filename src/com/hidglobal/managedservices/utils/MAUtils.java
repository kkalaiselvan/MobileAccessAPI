package com.hidglobal.managedservices.utils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.acti.jaas.ftress.principals.SimpleFtressPrincipal;
import com.hidglobal.managedservices.vo.Meta;

@Component
public class MAUtils {

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public Properties getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(Properties resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	public Meta setMeta(String resourceType, String resourceUrl,
			String[] locationDetails, String lastModifiedDt) {

		Meta meta = new Meta();
		meta.setResourceType(resourceType);
		resourceUrl = resourceLocation.getProperty(resourceUrl);
		System.out.println("Resource URL::" + resourceUrl);
		for (String s : locationDetails) {
			System.out.println("locationDetails:" + s);
			resourceUrl = stringBuilder(resourceUrl + "/" + s);
			System.out.println("resource url after concat:" + resourceUrl);
		}
		meta.setLocation(resourceUrl);
		meta.setLastModified(lastModifiedDt);
		return meta;

	}

	public List<String> formatRegex(String regexFilter) {

		List<String> filters = new ArrayList<String>();
		String[] retval = regexFilter.split(",");

		for (String value : retval) {
			filters.add(value);
		}

		return filters;

	}

	public String stringBuilder(String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			if (message != null) {
				sb.append(message);
			}
		}
		return sb.toString();
	}
	
	public boolean isValidDateTime(String dateTime){
		
		Pattern utcDateTimepattern = Pattern.compile("-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?)(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
									 
		Matcher mtch = utcDateTimepattern.matcher(dateTime);
		if(!mtch.matches()){
			return false;
		}		 
		return true;					 
	}
	
	public String getTimeZone(String dateTime){
		
		Pattern utcDateTimepattern = Pattern.compile("-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?)(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
		Matcher mtch = utcDateTimepattern.matcher(dateTime);
		mtch.reset();
		mtch.find();	
		return mtch.group(7);
	}
	
	public String getRequestor(HttpServletRequest request){
		
	   /*  Subject subject = (Subject)request.getSession().getAttribute("SUBJECT");
	      Set<Principal> principals = subject.getPrincipals();
	      String requestor=null;
	     for(Principal principal : principals){
		     
		     if(principal instanceof SimpleFtressPrincipal){
		    	 logger.debug("Principal instance type " + principal.getClass().getName() + ", value " + principal.getName());
		    	 requestor=principal.getName().toString();
		    	 return requestor;
		     }
	     
	     }*/
	     return "requestor";
	}

}
