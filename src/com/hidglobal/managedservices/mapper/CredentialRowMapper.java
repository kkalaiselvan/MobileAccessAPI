package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;

import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.MobileId;



public class CredentialRowMapper implements RowMapper<MobileId> {
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    Properties resourceLocation = (Properties) context.getBean("resourceLocation");
	@Override
	public MobileId mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		
	
		MobileId mobileId = new MobileId();
	
		mobileId.setId(rs.getLong("CREDENTIAL_ID"));
		mobileId.setPartNumber(rs.getString("PART_NUMBER"));
		mobileId.setPartnumberFriendlyName(rs.getString("FRIENDLY_NAME"));
		mobileId.setCardNumber(rs.getString("CARD_NUMBER"));
		mobileId.setReferenceNumber(rs.getString("REFERENCE_NUMBER"));
		mobileId.setStatus(rs.getString("CREDENTIAL_STATUS"));
		mobileId.setCredentialIdType(rs.getString("CREDENTIAL_TYPE"));
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.MOBILEIDRESOURCETYPE);
		
		meta.setId(mobileId.getId());
		
		meta.setLocation(resourceLocation.getProperty(HIDContants.MOBILEIDLOCATION).concat("/").concat(Long.toString(mobileId.getId())));
		meta.setLastModified(rs.getString("LAST_MODIFIED_DT"));
		mobileId.setMeta(meta);
		return mobileId;
}

	public Properties getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(Properties resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

}
