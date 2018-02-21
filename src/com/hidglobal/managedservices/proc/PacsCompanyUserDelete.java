package com.hidglobal.managedservices.proc;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import com.hidglobal.managedservices.mapper.DeleteCredentialRowMapper;
import com.hidglobal.managedservices.mapper.DeleteEndpointRowMapper;
import com.hidglobal.managedservices.mapper.DeleteInvitationRowMapper;



public class PacsCompanyUserDelete extends StoredProcedure {

	private static final String STORED_PROC_NAME="ma_company_user_utils.DELETE_INVENTORY";
	
	public PacsCompanyUserDelete(DataSource ds){
		
		super(ds,STORED_PROC_NAME);
		declareParameter(new SqlParameter("identifier", OracleTypes.VARCHAR));
		declareParameter(new SqlParameter("userArrayDB", OracleTypes.ARRAY));
		declareParameter(new SqlParameter("endPointArrayDB", OracleTypes.ARRAY));
		declareParameter(new SqlParameter("userName", OracleTypes.VARCHAR));
		declareParameter(new SqlOutParameter("invitationCursor", OracleTypes.CURSOR,new DeleteInvitationRowMapper()));
		declareParameter(new SqlOutParameter("mobileIdCursor", OracleTypes.CURSOR, new DeleteCredentialRowMapper()));
		declareParameter(new SqlOutParameter("deviceCursor", OracleTypes.CURSOR, new DeleteEndpointRowMapper()));
		declareParameter(new SqlOutParameter("retVal", OracleTypes.NUMBER));
		declareParameter(new SqlOutParameter("errorMess", OracleTypes.VARCHAR));
		compile();
	
	}
	
}
