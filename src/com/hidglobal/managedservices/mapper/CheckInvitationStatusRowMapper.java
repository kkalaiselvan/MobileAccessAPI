package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CheckInvitationStatusRowMapper implements RowMapper<Long>{

	@Override
	public Long mapRow(ResultSet resultSet, int rowNum) throws SQLException {
	
		return resultSet.getLong("STATUS_ID");
	}

}
