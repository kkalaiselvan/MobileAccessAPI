package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Invitation;
import com.hidglobal.managedservices.vo.Meta;

public class PacsInvitationRowMapper implements RowMapper<Invitation> {
	

	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    Properties resourceLocation = (Properties) context.getBean("resourceLocation");
	@Override
	public Invitation mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		Invitation invitation = new Invitation();
		//invitation.setId(rs.getLong("ID"));
		
		invitation.setAamkInvitationId(rs.getLong("AAMK_INVITATION_ID"));
	
		invitation.setInvitationCode(rs.getString("AAMK_INVITATION_CODE"));
		invitation.setCompanyUserId(rs.getLong("COMPANY_USER_ID"));
		invitation.setCreatedDate(rs.getString("CREATED_TS"));
		invitation.setExpirationDate(rs.getString("EXPIRATION_TS"));
		invitation.setStatus(rs.getString("INVITATION_STATUS"));
		
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.INVITATIONRESOURCETYPE);
		meta.setId(invitation.getAamkInvitationId());
		meta.setLocation(resourceLocation.getProperty(HIDContants.INVITATIONLOCATION).concat("/").concat(Long.toString(invitation.getAamkInvitationId())));
		meta.setLastModified(rs.getString("LAST_MODIFIED_DT"));
		
		invitation.setMeta(meta);
		return invitation;
}

	public Properties getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(Properties resourceLocation) {
		this.resourceLocation = resourceLocation;
	}
}
	
