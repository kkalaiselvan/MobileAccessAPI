package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;

import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Emails;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.Name;
import com.hidglobal.managedservices.vo.PacsUser;

public class UserRowMapper implements RowMapper<PacsUser> {
	
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    Properties resourceLocation = (Properties) context.getBean("resourceLocation");
	@Override
	public PacsUser mapRow(ResultSet rs, int rowNum) throws SQLException {

		PacsUser user = new PacsUser();
		Name name = new Name();
		Emails email = new Emails();
		user.setId(rs.getLong("id"));
		
		name.setFamilyName(rs.getString("last_name"));
		name.setGivenName(rs.getString("first_name"));
		user.setName(name);
		email.setValue(rs.getString("email"));
		List<Emails> emails = new ArrayList<Emails>();
		emails.add(email);
		user.setEmails(emails);
		user.setExternalId(rs.getString("user_id"));
		user.setStatus(rs.getString("user_status"));
		
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.USERRESOURCETYPE);
		meta.setLocation(resourceLocation.getProperty(HIDContants.USERLOCATION).concat("/").concat(Long.toString(user.getId())));
		meta.setLastModified(rs.getString("last_modified_dt"));
		user.setMeta(meta);
		return user;
	}

}
