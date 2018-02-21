package com.hidglobal.managedservices.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.mapper.CredentialRowMapper;
import com.hidglobal.managedservices.mapper.DeviceRowMapper;

import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.QueryBuilder;
import com.hidglobal.managedservices.vo.Device;

import com.hidglobal.managedservices.vo.MobileId;
import com.hidglobal.managedservices.vo.QueryBuilderResponse;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchDevicesResponse;

/**
 * This class mainly interacts with database for Device related information
 * through stored procedure and Queries .
 * 
 * 
 */
@Repository
public class DeviceDAO extends AbstractDAO {

	@Autowired
	QueryBuilder queryBuilder;
	private static final Logger logger = LoggerFactory
			.getLogger(DeviceDAO.class);
	@Autowired
	private UtilsDAO utilsDAO;

	/**
	 * This method interacts with database through query and fetch the Device
	 * details for the given request
	 * 
	 * @param sqlQuery
	 * @param paramsList
	 * @return
	 */
	private List<Device> getDeviceResultSet(String sqlQuery,
			List<Object> paramsList) {
		List<Device> deviceRowObject = new ArrayList<Device>();
		deviceRowObject = (List<Device>) jdbcTemplateObject.query(sqlQuery,
				paramsList.toArray(), new DeviceRowMapper());
		return deviceRowObject;
	}

	/**
	 * This method interacts with database through query and fetch the Device
	 * details for the given request
	 * 
	 * @param filterByIds
	 * @return
	 * @throws DBException
	 */
	public Device fetchDeviceDetails(QueryFilterId filterByIds)
			throws DBException {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.DEVICES,
				filterByIds);

		List<Device> deviceRowObject = getDeviceResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		Device device = new Device();

		if (deviceRowObject.size() != 0) {
			device = deviceRowObject.get(0);
		}

		else {
			logger.error("Get Device Details Failed for Device ID [{}]",
					filterByIds.getDeviceId());

			throw new DBException(String.valueOf(filterByIds.getDeviceId()),
					String.valueOf(HIDContants.INVALID_DEVICEID),
					HIDContants.NOTARGET);
		}
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.DEVICE_MOBILEID,
				filterByIds);
		List<MobileId> mobileIdsRowObject = getMobileIdsResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());
		if (!mobileIdsRowObject.isEmpty()) {
			List<MobileId> mobileIds = new ArrayList<MobileId>();

			for (MobileId mobileId : mobileIdsRowObject) {
				mobileId.setMeta(null);
				mobileIds.add(mobileId);
			}

			device.setMobileIds(mobileIds);
		}
		return device;

	}

	/**
	 * This method interacts with database through query and fetch the MobileIDs
	 * details for the given request
	 * 
	 * @param sqlQuery
	 * @param paramsList
	 * @return
	 */
	private List<MobileId> getMobileIdsResultSet(String sqlQuery,
			List<Object> paramsList) {

		List<MobileId> mobileIdsRowObject = new ArrayList<MobileId>();
		mobileIdsRowObject = (List<MobileId>) jdbcTemplateObject.query(
				sqlQuery, paramsList.toArray(), new CredentialRowMapper());
		return mobileIdsRowObject;

	}

	/**
	 * This method interacts with database through query and fetch the Device
	 * details for the given request
	 * 
	 * @param filterByIds
	 * @return
	 */
	public SearchDevicesResponse listDevices(QueryFilterId filterByIds) {
		// TODO Auto-generated method stub
		QueryBuilderResponse builderResponse;
		builderResponse = queryBuilder.sqlQueryBuilder(Schemas.DEVICES,
				filterByIds);

		int count = utilsDAO.getRowCounts(
				builderResponse.getQueryStringforCount(),
				builderResponse.getCountQueryParams());
		logger.debug("Total Devices Found for Company ID [{}] is [{}]",
				filterByIds.getCompanyId(), count);

		List<Device> deviceRowObject = getDeviceResultSet(
				builderResponse.getQueryString(),
				builderResponse.getQueryParams());

		List<Device> devices = new ArrayList<Device>();
		Map<String, Device> deviceMap = new HashMap<String, Device>();
		for (Device d : deviceRowObject) {
			deviceMap.put(d.getId().toString(), d);
		}

		for (Map.Entry<String, Device> entry : deviceMap.entrySet()) {
			Device device = new Device();
			device.setMeta(entry.getValue().getMeta());
			device.getMeta().setLocation(
					MessageFormat.format(device.getMeta().getLocation(),
							Long.toString(filterByIds.getCompanyId())));
			devices.add(device);

		}
		/*
		 * for (Device d : deviceRowObject) { Device device = new Device();
		 * device.setMeta(d.getMeta()); device.getMeta().setLocation(
		 * MessageFormat.format(device.getMeta().getLocation(),
		 * filterByIds.getCompanyId())); devices.add(device); }
		 */
		SearchDevicesResponse deviceSearchResult = new SearchDevicesResponse();

		deviceSearchResult.setTotalResults(deviceMap.size());
		deviceSearchResult.setItemsPerPage(deviceMap.size());
		deviceSearchResult.setStartIndex(QueryBuilder.START_INDEX);
		logger.debug("Items Per Page: [{}]",
				deviceSearchResult.getItemsPerPage());
		logger.debug("Start Index: [{}]", deviceSearchResult.getStartIndex());
		deviceSearchResult.setDevices(devices);
		return deviceSearchResult;
	}

}
