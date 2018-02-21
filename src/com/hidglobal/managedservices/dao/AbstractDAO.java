package com.hidglobal.managedservices.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractDAO {
	
	@Autowired
	protected DataSource dataSource;

	@Autowired
	protected JdbcTemplate jdbcTemplateObject;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		System.out.println("Setting DataSource:"+dataSource);
		this.dataSource = dataSource;
//		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public JdbcTemplate getJdbcTemplateObject() {
		return jdbcTemplateObject;
	}

	public void setJdbcTemplateObject(JdbcTemplate jdbcTemplateObject) {
		this.jdbcTemplateObject = jdbcTemplateObject;
	}
	
	
	
}
