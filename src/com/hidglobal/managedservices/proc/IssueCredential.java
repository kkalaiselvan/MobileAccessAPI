package com.hidglobal.managedservices.proc;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class IssueCredential extends StoredProcedure{
	private static final String STORED_PROC_NAME="ma_credential_utils. issue_credentials";

	public IssueCredential(DataSource ds) {
		super(ds, STORED_PROC_NAME);
		declareParameter(new SqlInOutParameter("pio_credential_id", Types.NUMERIC));
		declareParameter(new SqlParameter("pi_device_id", Types.NUMERIC));
		declareParameter(new SqlInOutParameter("pio_part_number", Types.VARCHAR));
		declareParameter(new SqlParameter("pi_company_id", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_freindly_name", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_card_number", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_reference_number", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_credential_status", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_credential_type", Types.VARCHAR));
		declareParameter(new SqlParameter("pi_requestor", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_last_modified_dt", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_retval", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_retmsg", Types.VARCHAR));
	}
	

}
