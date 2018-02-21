package com.hidglobal.managedservices.business;

import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.PacsUserActions;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchInvitationResponse;
import com.hidglobal.managedservices.vo.SearchRequest;

public interface IInvitationService {
	public Invitation getInvitationDetails(QueryFilterId filters)
			throws DBException;
	public SearchInvitationResponse listInvitation(QueryFilterId filters);
	public SearchInvitationResponse searchInvitation(SearchRequest searchRequest, QueryFilterId filterByIds)throws Exception ;
	public boolean cancelInvitation(long invitationID,String companyId)throws InvitationException , MobileAccessException , MAValidationException;
public void fetchSendInvitationDetails(String companyId,Long invitationId) throws MobileAccessException, Exception;
	

	/*public boolean cancelInvitationForUser(long userId, long invitationId,String companyId)throws InvitationException,InvitationException ,  MobileAccessException;*/
	
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PacsUser createInvitation(long compnayUserId,
			PacsUserActions pacsUserAction,String companyId,String requestor)throws Exception;
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PacsUser createInvitationForUser(long compnayUserId,
			PacsUserActions pacsUserAction,String companyId,String requestor,Long credentialValue)throws Exception;

	public Integer getInvitationExpiryTimeForCompanyInMin(
			List<Map<String, Object>> expiryTime) throws Exception;
}
