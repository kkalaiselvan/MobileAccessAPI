package com.hidglobal.managedservices.proc;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class SendInvitationEmail extends StoredProcedure{
	
	private static final String STORED_PROC_NAME ="Ma_invitation_utils.send_invitation_input";

	public SendInvitationEmail(DataSource ds) {
		super(ds,STORED_PROC_NAME);
		declareParameter(new SqlParameter("pi_company_id", Types.NUMERIC));
		declareParameter(new SqlParameter("pi_aamk_invitation_id", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_first_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_last_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_from", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_to", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_aamk_invitation_code", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_expiration_ts", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_subject", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_body", Types.VARCHAR));
		declareParameter(new SqlOutParameter("pi_requestor", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_retval", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_retmsg", Types.VARCHAR));
		
	}
	

}
