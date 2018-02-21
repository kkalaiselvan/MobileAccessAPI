package com.hidglobal.managedservices.dao;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.utils.HIDContants;

/**
 * 
 * This class mainly interacts with database for some common information through
 * Queries .
 * 
 */
@Component
public class UtilsDAO extends AbstractDAO {
	private static final Logger logger = LoggerFactory
			.getLogger(UtilsDAO.class);

	/**
	 * This method interacts with database and fetch the Mobile Access web
	 * service URl
	 * 
	 * @return
	 * @throws MobileAccessException
	 */
	public String fetchUrlFromDB() throws MobileAccessException {
		Object[] paramObject = new Object[] { HIDContants.SERVICE_URL };
		int[] argType = new int[] { Types.VARCHAR };
		String ServiceURL;
		try {
			ServiceURL = jdbcTemplateObject.queryForObject(
					HIDContants.SERVICE_UTIL_SQL,

					paramObject, argType, String.class);

			logger.debug("Mobile Access service URL [{}]",
					ServiceURL.toString());

		} catch (EmptyResultDataAccessException e) {

			throw new MobileAccessException("Service URL not Found");
		}
		return ServiceURL;
	}

	/**
	 * This method interacts with database through query and fetch the relay
	 * host name for sending email
	 * 
	 * @return
	 * @throws MobileAccessException
	 */
	public Map<String, String> fetchHostDetails() throws MobileAccessException {
		Map<String, String> hostDetails = new HashMap<String, String>();
		Object[] paramObject = null;
		int[] argType = null;
		String snmpHostName;
		try {
			paramObject = new Object[] { HIDContants.SNMP_HOST_NAME };
			argType = new int[] { Types.VARCHAR };
			snmpHostName = jdbcTemplateObject.queryForObject(
					HIDContants.SERVICE_UTIL_SQL, paramObject, argType, String.class);
			logger.debug("SNMP Host Name [{}]", snmpHostName);
			hostDetails.put("HostName", snmpHostName);
		}

		catch (EmptyResultDataAccessException e) {

			throw new MobileAccessException("SNMP Host Name not Found");
		}

		paramObject = new Object[] { HIDContants.SNMP_PORT_NO };
		argType = new int[] { Types.VARCHAR };
		String snmpHostPort;
		try {
			snmpHostPort = jdbcTemplateObject.queryForObject(
					HIDContants.SERVICE_UTIL_SQL, paramObject, argType, String.class);

			hostDetails.put("HostPort", snmpHostPort);
		} catch (EmptyResultDataAccessException e) {

			throw new MobileAccessException("SNMP Host Port not Found");
		}

		return hostDetails;

	}

	public int getRowCounts(String countQuery, List<Object> paramsList) {
		// TODO Auto-generated method stub
		int count = jdbcTemplateObject.queryForObject(countQuery,
				paramsList.toArray(), Integer.class);
		return count;

	}

}
