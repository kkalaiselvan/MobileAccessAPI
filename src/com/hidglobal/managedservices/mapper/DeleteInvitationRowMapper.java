package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.hidglobal.managedservices.vo.Invitation;

public class DeleteInvitationRowMapper implements RowMapper<Invitation> {

	@Override
	public Invitation mapRow(ResultSet resultset, int rowNum)
			throws SQLException {
		Invitation invitation = new Invitation();
		invitation.setAamkInvitationId(resultset.getLong("aamk_invitation_id"));
		return invitation;
	}

}
