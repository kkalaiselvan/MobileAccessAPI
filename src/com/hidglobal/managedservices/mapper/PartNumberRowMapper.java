package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.PartNumber;

public class PartNumberRowMapper implements RowMapper<PartNumber> {


		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	    Properties resourceLocation = (Properties) context.getBean("resourceLocation");

		@Override
		public PartNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			PartNumber partNumber = new PartNumber();
	
			partNumber.setId(rs.getLong("PN_HDR_ID"));
			partNumber.setPartNumber(rs.getString("PART_NUMBER"));
			partNumber.setFriendlyName(rs.getString("PARTNUMBER_FRIENDLY_NAME"));
			partNumber.setDescription(rs.getString("PARTNUMBER_DESCRIPTION"));
			partNumber.setAvailableQty(rs.getInt("AVAILABLE_QTY"));
	
			
			Meta meta = new Meta();
			meta.setResourceType(HIDContants.PARTNUMBERRESOURCETYPE);
			meta.setId(partNumber.getId());
			meta.setLocation(resourceLocation.getProperty(HIDContants.PARTNUMBERLOCATION).concat("/").concat(String.valueOf(partNumber.getId())));
			meta.setLastModified(rs.getString("LAST_MODIFIED_DT"));
			
			partNumber.setMeta(meta);
			return partNumber;
	}

		public Properties getResourceLocation() {
			return resourceLocation;
		}

		public void setResourceLocation(Properties resourceLocation) {
			this.resourceLocation = resourceLocation;
		}
	}
		
