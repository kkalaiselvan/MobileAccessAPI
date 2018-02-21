package com.hidglobal.managedservices.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;



public class MobileAccessAuthenticator implements ContainerRequestFilter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Override
    public ContainerRequest filter(ContainerRequest request) {
        try {
        	
        	logger.info("[Mobile Access API] ### Authentication Success ### {}", new Date());
        	logger.info("[Mobile Access API] Request URI {}", request.getAbsolutePath());
   

		} catch (Exception e) {
			logger.info("Exception =>  {}", e.toString());
		} 

       return request;        
    }
    
}
