package com.hidglobal.managedservices.proc;

import java.sql.Types;
import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import com.hidglobal.managedservices.mapper.EndpointCredentialRowMapper;
import com.hidglobal.managedservices.mapper.PacsInvitationRowMapper;
import com.hidglobal.managedservices.mapper.UserRowMapper;

public class PacsGetUser extends StoredProcedure {

	private static final String STORED_PROC_NAME = "ma_company_user_utils.get_inventory";

	public PacsGetUser(DataSource ds) {
		super(ds, STORED_PROC_NAME);
		declareParameter(new SqlInOutParameter("pio_company_id", Types.NUMERIC));
		declareParameter(new SqlInOutParameter("pio_company_user_id",
				Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_user_result_set",
				OracleTypes.CURSOR, new UserRowMapper()));
		declareParameter(new SqlOutParameter("po_invitation_result_set",
				OracleTypes.CURSOR, new PacsInvitationRowMapper()));
		declareParameter(new SqlOutParameter("po_endpt_credential_result_set",
				OracleTypes.CURSOR, new EndpointCredentialRowMapper()));
		declareParameter(new SqlParameter("pi_requestor", Types.VARCHAR));     
		declareParameter(new SqlOutParameter("po_retval", Types.NUMERIC));
		declareParameter(new SqlOutParameter("po_retmsg", Types.VARCHAR));
		compile();
		// TODO Auto-generated constructor stub
	}
}
