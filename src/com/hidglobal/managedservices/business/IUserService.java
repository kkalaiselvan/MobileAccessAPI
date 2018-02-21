package com.hidglobal.managedservices.business;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.SearchUserResponse;

public interface IUserService {

	public PacsUser enrollUser(PacsUser user) throws DBException,
			MobileAccessException, InvitationException, Exception;

	public PacsUser updateUser(PacsUser user) throws DBException,
			MobileAccessException;

	public PacsUser getUserDetails(PacsUser user) throws DBException,
			Exception;

	public SearchUserResponse listUsers(QueryFilterId filterByIds) ;

	public SearchUserResponse searchUsers(SearchRequest searchRequest, QueryFilterId filterByIds)throws  Exception;

	public int deleteUser(Long companyUserId,String companyId) throws Exception;

}
