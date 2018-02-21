package com.hidglobal.managedservices.security;

import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CacheControlFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {

		response.getHttpHeaders().putSingle(HttpHeaders.CACHE_CONTROL,
				"No-Store");
		response.getHttpHeaders().putSingle("Pragma", "No-Cache");
		response.getHttpHeaders().putSingle(HttpHeaders.EXPIRES, "-1");
		return response;

	}

}
