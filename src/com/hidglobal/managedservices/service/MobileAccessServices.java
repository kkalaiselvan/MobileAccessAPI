package com.hidglobal.managedservices.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceException;
import org.springframework.ws.client.core.WebServiceTemplate;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.mobileaccess.CancelInvitationRequestType;
import com.hidglobal.managedservices.mobileaccess.CreateInvitationRequestType;
import com.hidglobal.managedservices.mobileaccess.CreateInvitationResponseType;
import com.hidglobal.managedservices.mobileaccess.CredentialType;
import com.hidglobal.managedservices.mobileaccess.CredentialsType;
import com.hidglobal.managedservices.mobileaccess.DeleteDeviceRequestType;
import com.hidglobal.managedservices.mobileaccess.DeviceType;
import com.hidglobal.managedservices.mobileaccess.DevicesType;
import com.hidglobal.managedservices.mobileaccess.InvitationType;
import com.hidglobal.managedservices.mobileaccess.InvitationsType;
import com.hidglobal.managedservices.mobileaccess.IssueCredentialRequestType;
import com.hidglobal.managedservices.mobileaccess.RevokeCredentialRequestType;
import com.hidglobal.managedservices.utils.AppCache;
import com.hidglobal.managedservices.vo.Invitation;

/**
 * 
 * This class is responsible for Invoking web services such as 1.Cancel
 * Invitation 2.Revoke Credential 3.Create Invitation 4.Delete Device
 * 
 */
@Component
public class MobileAccessServices {

	@Autowired
	private WebServiceTemplate webServiceTemplate;
	@Autowired
	private AppCache appCache;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * This method will cancel all the requested pending Invitation.This is
	 * Asynchronous call so there will not be any response.
	 * 
	 * @param invitationIds
	 *            This holds the List of Invitations that need to be cancelled.
	 * @return This returns true on successful Execution of web service call
	 * @throws MobileAccessException
	 */
	public boolean cancelInvitation(Long[] invitationIds)
			throws MobileAccessException {
		boolean result = false;

		if (invitationIds == null || invitationIds.length == 0) {
			return result;
		}

		InvitationsType invitationsType = new InvitationsType();

		List<InvitationType> invitationId = invitationsType.getInvitation();
		for (Long id : invitationIds) {
			logger.info("Cancel Invitation Requested for AAMK - Id [{}]", id);
			InvitationType invitationType = new InvitationType();
			invitationType.setInvitationId(id);
			invitationId.add(invitationType);
		}

		CancelInvitationRequestType cancelInvitationRequestType = new CancelInvitationRequestType();
		cancelInvitationRequestType.setInvitations(invitationsType);
		webServiceTemplate.setDefaultUri(appCache.getServiceUrl());
		webServiceTemplate.marshalSendAndReceive(cancelInvitationRequestType);
		return true;
	}

	/**
	 * This method is responsible to revoke credential for all the requested
	 * Endpoints and credentials.This is Asynchronous call so there will not be
	 * any response.
	 * 
	 * @param endpointToCredIdsMap
	 *            This holds the Endpoints and List of credentials associated
	 *            with the Endpoints.
	 * @return This returns true on successful Execution of web service call
	 * @throws WebServiceException
	 */
	@SuppressWarnings("unchecked")
	public boolean revokeCredentialProcess(
			Map<String, Object> endpointToCredIdsMap)

	throws WebServiceException, javax.xml.ws.WebServiceException,
			MobileAccessException {
		Map<Long, List<Long>> endpointIdToCredIdsMap = (Map<Long, List<Long>>) endpointToCredIdsMap
				.get("CREDENTIAL_MAP_KEY");

		boolean result = true;
		if (endpointIdToCredIdsMap == null || endpointIdToCredIdsMap.isEmpty()) {
			return false;
		}

		/*
		 * RevokeCredentialRequestType revokeCredentialRequestType = new
		 * RevokeCredentialRequestType();
		 */
		CredentialsType credentialsType = new CredentialsType();
		List<CredentialType> credentialsList = credentialsType.getCredential();

		for (Long epId : endpointIdToCredIdsMap.keySet()) {
			for (Long credId : endpointIdToCredIdsMap.get(epId)) {
				CredentialType credentialType = new CredentialType();
				logger.info(
						"Revoke Credential Requested -Crdential Id [{}] , Endpoint Id [{}]",
						credId, epId);
				credentialType.setCredentialId(credId);
				credentialType.setEndpointId(epId);
				credentialsList.add(credentialType);
			}

		}
		revokeMobileId(credentialsType);
		/*
		 * revokeCredentialRequestType.setCredentials(credentialsType);
		 * webServiceTemplate.setDefaultUri(appCache.getServiceUrl());
		 * webServiceTemplate
		 * .marshalSendAndReceive(revokeCredentialRequestType);
		 */
		return result;

	}

	/**
	 * This method is responsible for deleting the requested device.This is
	 * Asynchronous call so there will not be any response.
	 * 
	 * @param deviceIds
	 *            This holds list of DeviceId to be deleted.
	 * @return This returns true on successful Execution of web service call
	 * 
	 * @throws MobileAccessException
	 */

	public boolean deleteDevice(Long[] deviceIds) throws MobileAccessException {
		boolean result = false;
		if (deviceIds == null || deviceIds.length == 0) {
			return result;
		}

		DeleteDeviceRequestType deleteDeviceRequestType = new DeleteDeviceRequestType();
		DevicesType devicesType = new DevicesType();
		List<DeviceType> devicesList = devicesType.getDevice();
		for (Long endpId : deviceIds) {
			logger.info("Delete Device Requested- Endpoint Id [{}]", endpId);
			DeviceType deviceType = new DeviceType();
			deviceType.setEndpointId(endpId);
			devicesList.add(deviceType);
		}

		deleteDeviceRequestType.setDevices(devicesType);
		webServiceTemplate.setDefaultUri(appCache.getServiceUrl());
		webServiceTemplate.marshalSendAndReceive(deleteDeviceRequestType);
		return true;
	}

	/**
	 * This method is responsible for creating new Invitation.This call will
	 * respond synchronously with newly created Invitation code.
	 * 
	 * @param expiryTimeinMinutes
	 *            This holds the expire time in minutes to be assigned for the
	 *            invitation
	 * @return This holds all the Information about newly created Invitation
	 * @throws MobileAccessException
	 */
	public Invitation createInvitation(Long expiryTimeinMinutes)
			throws MobileAccessException {
		CreateInvitationRequestType createInvitationRequest = new CreateInvitationRequestType();
		createInvitationRequest.setExpiryTimeInMinutes(expiryTimeinMinutes);
		logger.info(
				"Create Invitation Requested - Exipry Time in Minutes [{}]",
				expiryTimeinMinutes);
		webServiceTemplate.setDefaultUri(appCache.getServiceUrl());
		CreateInvitationResponseType response = (CreateInvitationResponseType) webServiceTemplate
				.marshalSendAndReceive(createInvitationRequest);
		Invitation invitation = new Invitation();
		invitation.setInvitationCode(response.getInvitationCode());
		invitation.setAamkInvitationId(response.getInvitationId());
		invitation.setCreatedDate(response.getCreatedAt().toString());
		invitation.setExpirationDate(response.getExpiresAt().toString());
		invitation.setStatus(response.getStatus().value());
		return invitation;

	}

	/**
	 * This method is responsible to revoke all the requested credentials .This
	 * is Asynchronous call so there will not be any response.
	 * 
	 * @param credentials
	 *            This holds the List of endpoints and credentials associated
	 *            with the endpoints.
	 * @throws MobileAccessException
	 */
	public void revokeMobileId(CredentialsType credentials)
			throws MobileAccessException {
		logger.info("Credential Revoked for the Endpoint Id [{}]", credentials
				.getCredential().get(0).getEndpointId());
		RevokeCredentialRequestType revokeCredentialRequestType = new RevokeCredentialRequestType();
		revokeCredentialRequestType.setCredentials(credentials);
		webServiceTemplate.setDefaultUri(appCache.getServiceUrl());
		webServiceTemplate.marshalSendAndReceive(revokeCredentialRequestType);
	}

	/**
	 * This method is responsible for Issuing requested credentials to the
	 * requested endpoints.This is Asynchronous call so there will not be any
	 * response.
	 * 
	 * @param credentials
	 *         This holds the List of endpoints and credentials associated
	 *            with the endpoints.
	 * @throws Exception
	 */
	public void issueMobileId(CredentialsType credentials) throws Exception {
		logger.info("Credential Issued for the Endpoint [{}]", credentials
				.getCredential().get(0).getEndpointId());
		IssueCredentialRequestType issueCredentialRequestType = new IssueCredentialRequestType();
		issueCredentialRequestType.setCredentials(credentials);
		webServiceTemplate.setDefaultUri(appCache.getServiceUrl());
		webServiceTemplate.marshalSendAndReceive(issueCredentialRequestType);
	}

}
