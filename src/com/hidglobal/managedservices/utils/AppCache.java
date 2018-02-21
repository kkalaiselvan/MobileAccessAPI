package com.hidglobal.managedservices.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.dao.UtilsDAO;
import com.hidglobal.managedservices.exception.MobileAccessException;

@Component
public class AppCache {

	@Autowired
	UtilsDAO utilsDAO;

	private String serviceUrl = null;
	private Map<String, String> hostDetails = null;

	public String getServiceUrl() throws MobileAccessException {

		if (serviceUrl == null) {
			synchronized (this) {
				if (serviceUrl == null) {
					serviceUrl = utilsDAO.fetchUrlFromDB();
				}
			}
		}
		return serviceUrl;
	}

	public Map<String, String> getHostDetails() throws MobileAccessException {
		if (hostDetails == null) {
			synchronized (this) {
				if (hostDetails == null) {
					hostDetails = utilsDAO.fetchHostDetails();
				}
			}
		}
		return hostDetails;
	}
}
