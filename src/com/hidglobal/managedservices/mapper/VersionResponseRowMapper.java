package com.hidglobal.managedservices.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.hidglobal.managedservices.vo.Version;

public class VersionResponseRowMapper implements RowMapper<Version> {

	public Version mapRow(ResultSet rs, int rowNum) throws SQLException {
		Version version = new Version();
		version.setVersion(rs.getString("VALUE"));
		version.setLastModifiedDt(rs.getString("LAST_MODIFIED_DT"));
		return version;
	}

}
