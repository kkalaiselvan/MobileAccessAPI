package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.hidglobal.managedservices.vo.Device;

public class DeleteEndpointRowMapper implements RowMapper<Device>{

	@Override
	public Device mapRow(ResultSet resultset, int rowNum) throws SQLException {
		Device device=new Device();
		device.setId(resultset.getLong("ENDPOINT_ID"));
		return device;
	}

}
