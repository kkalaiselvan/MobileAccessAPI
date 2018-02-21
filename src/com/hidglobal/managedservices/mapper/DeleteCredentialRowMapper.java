package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.vo.MobileId;

public class DeleteCredentialRowMapper implements RowMapper<MobileId>{

	@Override
	public MobileId mapRow(ResultSet resultset, int rowNum) throws SQLException {
		
		MobileId mobileId=new MobileId();
		mobileId.setId(resultset.getLong("CREDENTIAL_ID"));
		mobileId.setEndpointId(resultset.getLong("ENDPOINT_ID"));
		return mobileId;
	}

}
