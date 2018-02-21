package com.hidglobal.managedservices.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.mapper.VersionResponseRowMapper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.Version;
import com.hidglobal.managedservices.vo.VersionResponse;
import com.hidglobal.managedservices.vo.Schema.Schemas;

@Component
public class VersionDAO extends AbstractDAO {

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;

	private static final Logger logger = LoggerFactory
			.getLogger(VersionDAO.class);

	public VersionResponse getVersion(long companyId)
			throws MobileAccessException {
		/*Object[] paramObject = new Object[] { HIDContants.API_VERSION };
		int[] argType = new int[] { Types.VARCHAR };
		String version_value;
		try {
			version_value = jdbcTemplateObject.queryForObject(
					HIDContants.UTIL_SQL,

					paramObject, argType, String.class);

			logger.info(" Mobile Access version [{}]", version_value.toString());

		} catch (EmptyResultDataAccessException e) {

			throw new MobileAccessException("Version not Found");
		}*/
		Version version = (Version)getJdbcTemplateObject().queryForObject(
				HIDContants.VERSION_UTIL_SQL, new Object[] { HIDContants.API_VERSION  }, new VersionResponseRowMapper());
		VersionResponse versionResponse = new VersionResponse();
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.VERSIONRESOURCETYPE);
		meta.setLocation(MessageFormat.format(
				resourceLocation.getProperty(HIDContants.VERSIONLOCATION),
				new Long(companyId).toString()));
		meta.setLastModified(version.getLastModifiedDt());
		List<String> schemaList = new ArrayList<String>();
		schemaList.add(Schemas.VERSION_REQUEST.toString());
		versionResponse.setMeta(meta);
		versionResponse.setSchemas(schemaList);
		
		versionResponse.setVersion(version);
		return versionResponse;

	}
}
