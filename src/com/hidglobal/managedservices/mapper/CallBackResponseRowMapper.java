package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.vo.CallBackRequest;

public class CallBackResponseRowMapper implements RowMapper<CallBackRequest>{


	public CallBackRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
		CallBackRequest callBackRequest = new CallBackRequest();
		callBackRequest.setUrl(rs.getString("CALLBACK_URL"));
		callBackRequest.setLastModifiedDt(rs.getString("LAST_MODIFIED_DATE"));
		return callBackRequest;
	}

	
}
