package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class CheckExpirationMinuteRowMapper implements RowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet resultset, int rowNum) throws SQLException {
		
		Map<String, Object> outParam=new HashMap<String, Object>();
		outParam.put("VALUE", resultset.getObject("VALUE"));
		return outParam;
	}

}
