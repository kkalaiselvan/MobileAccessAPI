package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Device;
import com.hidglobal.managedservices.vo.Meta;


public class DeviceRowMapper implements RowMapper<Device> {
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    Properties resourceLocation = (Properties) context.getBean("resourceLocation");
	@Override
	public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
		Device device = new Device();
		
		device.setId(rs.getLong("ENDPOINT_ID"));
		device.setStatus(rs.getString("ENPOINT_STATUS"));
		device.setOsVersion(rs.getString("OS_VERSION"));
		device.setSecureElementType(rs.getString("SECURE_ELEMENT_TYPE"));
		device.setManufacturer(rs.getString("ENDPOINT_MANUFACTURER"));
		device.setModel(rs.getString("ENDPOINT_MODEL"));
		device.setApplicationVersion(rs.getString("APPLICATION_VERSION"));
		device.setSimOperator(rs.getString("SIM_OPERATOR"));
		device.setBluetoothCapability(rs.getString("BLE_CAPABILITY"));
		device.setNfcCapability(rs.getString("NFC_CAPABILITY"));
		
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.DEVICERESOURCETYPE);
		meta.setId(device.getId());
		meta.setLocation(resourceLocation.getProperty(HIDContants.DEVICELOCATION).concat("/").concat(Long.toString(device.getId())));
		meta.setLastModified(rs.getString("LAST_MODIFIED_DT"));
		device.setMeta(meta);
		return device;
	}

}
