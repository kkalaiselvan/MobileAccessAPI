package com.hidglobal.managedservices.proc;

import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class RegisterationDelete extends StoredProcedure {

private static final String STORED_PROC_NAME="ma_callback_utils.unregister_callback_url";
	
	public RegisterationDelete(DataSource ds) {
		super(ds, STORED_PROC_NAME);
		declareParameter(new SqlParameter("pi_requestor", Types.VARCHAR));
		declareParameter(new SqlInOutParameter("pio_company_id", Types.NUMERIC));
		declareParameter(new SqlInOutParameter("pio_registration_id", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_retval", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_retmsg", Types.VARCHAR));
				compile();
	}
}
