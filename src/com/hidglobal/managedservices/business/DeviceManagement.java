package com.hidglobal.managedservices.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hidglobal.managedservices.dao.DeviceDAO;
import com.hidglobal.managedservices.dao.UserDAO;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.service.MobileAccessServices;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchDevicesResponse;

/**
 * Device Management class act as a mediator to device related requests.It has
 * the implementation of Business Logic of where the Request needs to delegated
 * for the operation to be executed successfully. This class handles the
 * following operations.1.Delete device 2.Get Device Details 3.Search Device
 * Details
 * 
 * 
 */

@Service
public class DeviceManagement {
	@Autowired
	private DeviceDAO devicedao;
	@Autowired
	private UserDAO userdao;
	@Autowired
	private MobileAccessServices maServices;
	private static final Logger logger = LoggerFactory
			.getLogger(DeviceManagement.class);

	/**
	 * This method is responsible for mapping and routing device request to the
	 * UserDAO .
	 * 
	 * @param deviceId
	 *            This holds the endpointId to be deleted
	 * @param companyId
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void deleteDevice(Long deviceId, Long companyId) throws Exception {
		Long companyUserId = userdao.fetchCompanyUserIdfromEndpoint(deviceId,
				companyId);

		List<Long> identifierId = new ArrayList<Long>();
		List<Long> endpointId = new ArrayList<Long>();
		Map<String, Object> resultSetMap = null;
		identifierId.add(companyUserId);
		endpointId.add(deviceId);
		logger.info("Delete User Requested - company user Id[{}]",
				companyUserId);
		resultSetMap = userdao.getDeleteResultSet(identifierId, endpointId,
				HIDContants.REQUESTOR, HIDContants.DELETE_ENDPOINTS_IDENTIFIER);
		List<Invitation> invitationCursor = (List<Invitation>) resultSetMap
				.get("invitationCursor");

		List<MobileId> mobileIdCursor = (List<MobileId>) resultSetMap
				.get("mobileIdCursor");

		List<Device> deviceIdCursor = (List<Device>) resultSetMap
				.get("deviceCursor");
		BigDecimal retval = (BigDecimal) resultSetMap.get("retVal");
		Long[] invitationList = null;
		if (retval.intValue() == 0) {
			if (invitationCursor != null) {
				invitationList = new Long[invitationCursor.size()];

				for (int i = 0; i < invitationCursor.size(); i++) {
					invitationList[i] = invitationCursor.get(i)
							.getAamkInvitationId();

				}

			}

			List<Long> credIdList = null;
			if (mobileIdCursor != null) {

				Map<Long, List<Long>> deviceToMobileIdMap = new HashMap<Long, List<Long>>();

				for (MobileId mobileId : mobileIdCursor) {
					Long wsDeviceId = mobileId.getEndpointId();
					Long wsMobileId = mobileId.getId();
					credIdList = deviceToMobileIdMap.get(wsDeviceId);
					if (credIdList == null) {
						credIdList = new ArrayList<Long>();

					}
					credIdList.add(wsMobileId);
					deviceToMobileIdMap.put(wsDeviceId, credIdList);

				}
				resultSetMap.put("CREDENTIAL_MAP_KEY", deviceToMobileIdMap);

			}

			Long[] wsDeviceIds = null;

			if (deviceIdCursor != null) {

				wsDeviceIds = new Long[deviceIdCursor.size()];

				for (int i = 0; i < deviceIdCursor.size(); i++) {
					wsDeviceIds[i] = deviceIdCursor.get(i).getId();
				}
			}

			maServices.cancelInvitation(invitationList);

			maServices.revokeCredentialProcess(resultSetMap);

			maServices.deleteDevice(wsDeviceIds);

		}

	}

	/**
	 * This method routes the Get Device request to DeviceDAO class.
	 * 
	 * @param filterByIds
	 * @return
	 * @throws DBException
	 * @throws Exception
	 */
	public Device getDeviceDetails(QueryFilterId filterByIds)
			throws DBException, Exception {

		return devicedao.fetchDeviceDetails(filterByIds);
	}

	/**
	 * This method routes the List Device request to DeviceDAO class
	 * 
	 * @param filterByIds
	 * @return
	 */
	public SearchDevicesResponse listDevices(QueryFilterId filterByIds) {
		return devicedao.listDevices(filterByIds);
	}

}
