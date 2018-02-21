package com.hidglobal.managedservices.proc;

import java.sql.Types;
import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class PacsCompanyUserUpsert extends StoredProcedure {

	private static final String STORED_PROC_NAME = "ma_company_user_utils.upsert_company_user";

	public PacsCompanyUserUpsert(DataSource ds) {
		super(ds, STORED_PROC_NAME);
		declareParameter(new SqlInOutParameter("pio_user_id", Types.VARCHAR));
		declareParameter(new SqlInOutParameter("pio_first_name", Types.VARCHAR));
		declareParameter(new SqlInOutParameter("pio_last_name", Types.VARCHAR));
		declareParameter(new SqlInOutParameter("pio_email", Types.VARCHAR));
		declareParameter(new SqlInOutParameter("pio_company_id", Types.NUMERIC));
		declareParameter(new SqlParameter("pio_requestor", Types.VARCHAR));
		declareParameter(new SqlInOutParameter("pio_company_user_id", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_status", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_last_modified_dt", Types.VARCHAR));
		declareParameter(new SqlOutParameter("po_retval", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_retmsg", Types.VARCHAR));

		compile();
		// TODO Auto-generated constructor stub
	}

}
