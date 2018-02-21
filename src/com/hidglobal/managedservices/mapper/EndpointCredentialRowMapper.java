package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.MobileId;

public class EndpointCredentialRowMapper implements RowMapper<Device> {

	@Override
	public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Device device = new Device();
		device.setId(rs.getLong("ID"));
		device.setStatus(rs.getString("ENDPOINT_STATUS"));
		device.setOsVersion(rs.getString("OS_VERSION"));
		device.setSecureElementType(rs.getString("SECURE_ELEMENT_TYPE"));
		device.setManufacturer(rs.getString("ENDPOINT_MANUFACTURER"));
		device.setModel(rs.getString("ENDPOINT_MODEL"));
		device.setApplicationVersion(rs.getString("APPLICATION_VERSION"));
		device.setSimOperator(rs.getString("SIM_OPERATOR"));
		device.setBluetoothCapability(rs.getString("BLE_CAPABILITY"));
		device.setNfcCapability(rs.getString("NFC_CAPABILITY"));
		MobileId mobileId = new MobileId();
		mobileId.setId(rs.getLong("CREDENTIAL_ID"));
		if(mobileId.getId()==0)
			mobileId.setId(null);
		mobileId.setPartNumber(rs.getString("PART_NUMBER"));
		mobileId.setPartnumberFriendlyName(rs.getString("FRIENDLY_NAME"));
		mobileId.setCardNumber(rs.getString("CARD_NUMBER"));
		mobileId.setReferenceNumber(rs.getString("REFERENCE_NUMBER"));
		mobileId.setStatus(rs.getString("CREDENTIAL_STATUS"));
		mobileId.setCredentialIdType(rs.getString("CREDENTIAL_TYPE"));
		device.setMobileId(mobileId);
		return device;
	}

}
