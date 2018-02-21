package com.hidglobal.managedservices.business;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hidglobal.managedservices.dao.MobileIdsDAO;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.mobileaccess.CredentialType;
import com.hidglobal.managedservices.mobileaccess.CredentialsType;
import com.hidglobal.managedservices.service.MobileAccessServices;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchMobileIdResponse;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.UserMobileAction;

/**
 * 
 * 
 * This class act as a Mediator for all the MobileIds related requests. It has
 * the implementation of Business Logic of where the Request needs to delegated
 * for the operation to be executed successfully.This class handles following
 * requests 1.Get MobileId 2.List MobileIds 3.Search MobileId 4.Issue MobileId
 * 5.Revoke MobileId
 * 
 */
@Service
public class MobileIdsManagement {

	@Autowired
	private MobileIdsDAO mobileIdsDAO;
	@Autowired
	private MobileAccessServices mobileAccessService;

	private static final Logger logger = LoggerFactory
			.getLogger(MobileIdsManagement.class);

	/**
	 * This method routes Get MobileId requests to MobileIdDAO class
	 * 
	 * @param filterByIds
	 *            This holds the MobileId Value.
	 * 
	 * @throws DBException
	 */
	public MobileId getMobileIdDetails(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		return mobileIdsDAO.fetchMobileIdDetails(filterByIds);
	}

	/**
	 * This method routes List MobileId requests to MobileIdDAO class
	 * 
	 * @param filterByIds
	 * @throws DBException
	 */
	public SearchMobileIdResponse listMobileIds(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		return mobileIdsDAO.listMobileIds(filterByIds,
				HIDContants.MOBILE_ENTITY);
	}

	/**
	 * This method routes List Device Specific MobileId requests to MobileIdDAO
	 * class
	 * 
	 * @param filterByIds
	 * @throws DBException
	 */
	public SearchMobileIdResponse listDeviceMobileIds(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		return mobileIdsDAO.listMobileIds(filterByIds,
				HIDContants.DEVICE_ENTITY);
	}

	/**
	 * This method routes Search MobileId requests to MobileIdDAO class
	 * 
	 * @param searchRequest
	 * @param filterByIds
	 * @throws Exception
	 */
	public SearchMobileIdResponse searchMobileIds(SearchRequest searchRequest,
			QueryFilterId filterByIds) throws Exception {
		// TODO Auto-generated method stub
		return mobileIdsDAO.searchMobileIds(searchRequest, filterByIds);
	}

	public MobileIdsDAO getMobileIdsDAO() {
		return mobileIdsDAO;
	}

	public void setMobileIdsDAO(MobileIdsDAO mobileIdsDAO) {
		this.mobileIdsDAO = mobileIdsDAO;
	}

	/**
	 * This method routes Issue MobileId request to MobileIdDAO class. On
	 * successful return from DAO class ,it will invoke MobileAccessService to
	 * Issue credential.
	 * 
	 * @param companyId
	 * @param deviceId
	 * @param requestor
	 * @param userMobileAction
	 * @throws Exception
	 */

	public MobileId issueMobileId(Long companyId, Long deviceId,
			String requestor, UserMobileAction userMobileAction)
			throws Exception {
		MobileId mobileId = mobileIdsDAO.issueMobileId(companyId, deviceId,
				requestor, userMobileAction);
		/*if (mobileId != null) {
			mobileIdsDAO.initiateIssueMobileId(mobileId.getId());
		}*/
		CredentialsType credentialsType = new CredentialsType();
		List<CredentialType> credentialsList = credentialsType.getCredential();
		CredentialType credentialType = new CredentialType();
		credentialType.setCredentialId(mobileId.getId());
		credentialType.setEndpointId(deviceId);
		credentialsList.add(credentialType);
		mobileAccessService.issueMobileId(credentialsType);
		return mobileId;
	}

	/**
	 * This method routes Revoke Credential request to MobileIdDAO Class. On
	 * Successful return from DAO Class ,it will invoke MobileAccess service to
	 * Revoke credential.
	 * 
	 * @param companyId
	 * @param mobileId
	 * @throws Exception
	 */
	public void deleteMobileId(Long companyId, Long mobileId) throws Exception {
		Long endpointId;
		Long credentalStatus;
		credentalStatus = mobileIdsDAO.checkCredentialStatus(companyId,
				mobileId);
		if ((credentalStatus == 750) || (credentalStatus == 751)
				|| (credentalStatus == 753) || (credentalStatus == 756)) {
			endpointId = mobileIdsDAO.fetchEndpointId(mobileId);
			if (endpointId != null) {
				mobileIdsDAO.initateRevokeMobileId(mobileId);
				CredentialsType credentialsType = new CredentialsType();
				List<CredentialType> credentialsList = credentialsType
						.getCredential();
				CredentialType credentialType = new CredentialType();
				credentialType.setCredentialId(mobileId);
				credentialType.setEndpointId(endpointId);
				credentialsList.add(credentialType);
				logger.debug(
						"Revoke Credential Initiated for EdpointId-[{}] and MobileId-[{}]",
						endpointId, mobileId);
				mobileAccessService.revokeMobileId(credentialsType);
			} else {
				throw new MobileIdException(mobileId.toString(), null,
						"invalid.credential", HIDContants.MUTABILITY,
						HIDContants.PRECONDITIONFAILED);
			}
		} else if (credentalStatus == 755) {
			throw new MobileIdException(mobileId.toString(), null, "CS-755",
					HIDContants.MUTABILITY, HIDContants.PRECONDITIONFAILED);
		} else {
			throw new MobileIdException(mobileId.toString(), null,
					"invalid.credential", HIDContants.MUTABILITY,
					HIDContants.PRECONDITIONFAILED);
		}

	}

}
