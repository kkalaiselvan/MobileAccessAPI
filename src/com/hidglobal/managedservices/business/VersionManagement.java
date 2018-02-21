package com.hidglobal.managedservices.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.dao.VersionDAO;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.vo.VersionResponse;


@Component
public class VersionManagement {

	@Autowired
	private VersionDAO versionDAO;
	
	public VersionResponse getVersion(long companyId) throws MobileAccessException{
		return versionDAO.getVersion(companyId);
	}
}
