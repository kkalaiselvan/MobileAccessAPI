package com.hidglobal.managedservices.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.dao.RegisterationDAO;
import com.hidglobal.managedservices.vo.CallBackUrlRequest;


@Component
public class RegisterationManagement {


	
	@Autowired
	private RegisterationDAO registerationDAO;
	public CallBackUrlRequest doRegister(long companyId,CallBackUrlRequest registerationRequest,String requestor) throws Exception{
		return registerationDAO.doRegisteration(companyId, registerationRequest,requestor);
	
	}
	
	public String doUnRegister(long companyId,long registrationId,String requestor) throws Exception{
		return registerationDAO.doUnregistration(companyId, registrationId,requestor);
	
	}
	
	public CallBackUrlRequest doUpdate(long companyId,CallBackUrlRequest registerationRequest,String requestor) throws Exception{
		return registerationDAO.doUpdateRegisteration(companyId, registerationRequest,requestor);
	
	}
	

	public CallBackUrlRequest doGet(long companyId,long registrationId) throws Exception{
		return registerationDAO.doGetRegisteration(companyId, registrationId);
	
	}
}
