package com.hidglobal.managedservices.dao;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import com.hidglobal.managedservices.exception.RegisterationException;
import com.hidglobal.managedservices.mapper.CallBackResponseRowMapper;
import com.hidglobal.managedservices.proc.RegisterationDelete;
import com.hidglobal.managedservices.proc.RegisterationUpsert;
import com.hidglobal.managedservices.proc.RegistrationUpdate;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.CallBackUrlRequest;
import com.hidglobal.managedservices.vo.CallBackRequest;
import com.hidglobal.managedservices.vo.Meta;
import com.hidglobal.managedservices.vo.Schema.Schemas;

@Repository
public class RegisterationDAO extends AbstractDAO {

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;

	private final static Logger logger = LoggerFactory
			.getLogger(RegisterationDAO.class);

	public CallBackUrlRequest doRegisteration(long companyId,
			CallBackUrlRequest registerationRequest,String requestor) throws Exception {

		RegisterationUpsert registerationUpsert = new RegisterationUpsert(
				this.dataSource);
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("pi_requestor", requestor.trim());
		inParams.put("pio_company_id", companyId);
		inParams.put("pio_url", registerationRequest.getCallBackURL().getUrl()
				.toString().trim());
		Map<String, Object> outParams;

		outParams = registerationUpsert.execute(inParams);
		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();

		if (po_retval == -20098) {
			throw new RegisterationException(null, null, "url.exists",
					HIDContants.MUTABILITY, HIDContants.PRECONDITIONFAILED);
		}
		if (po_retval == -20096) {
			throw new RegisterationException(null, null, "company.invalid",
					HIDContants.NOTARGET, HIDContants.REQUESTNOTFOUND);
		}
		CallBackUrlRequest callBackRegisterationResponse = new CallBackUrlRequest();
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.REGISTRATIONRESOURCETYPE);
		meta.setLocation(MessageFormat.format(
				resourceLocation.getProperty(HIDContants.CALLBACKLOCATION),
				new Long(companyId).toString()).concat("/"+outParams.get("po_registration_id").toString()));
		meta.setLastModified(outParams.get("po_last_modified_dt").toString());
		callBackRegisterationResponse.setMeta(meta);
		CallBackRequest callBackRequest = new CallBackRequest();
		callBackRequest.setUrl(outParams.get("pio_url").toString());
		/*callBackRequest.setId(new Long(outParams.get("po_registration_id")
				.toString()));*/
		callBackRegisterationResponse.setCallBackURL(callBackRequest);
		callBackRegisterationResponse.setId(new Long(outParams.get("po_registration_id")
				.toString()));
		List<String> schemaList = new ArrayList<String>();
		schemaList.add(Schemas.NOTIFICATION_REQUEST.toString());
		callBackRegisterationResponse.setSchemas(schemaList);
		logger.info(
				"callback registration success  for company Id [{}] and the registration Id returned is [{}]  ",
				companyId, outParams.get("po_registration_id"));

		return callBackRegisterationResponse;

	}

	public String doUnregistration(long companyId, long registrationId,String requestor)
			throws Exception {
		RegisterationDelete registrationDelete = new RegisterationDelete(
				this.dataSource);
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("pi_requestor", requestor.trim());
		inParams.put("pio_company_id", companyId);
		inParams.put("pio_registration_id", registrationId);
		Map<String, Object> outParams = new HashMap<String, Object>();
		outParams = registrationDelete.execute(inParams);
		Integer retval = new Integer(outParams.get("po_retval").toString());

		logger.info(
				"Callback Un registration response status for the company Id [{}] from procedure is[{}] ", companyId,
				outParams.get("po_retval").toString());
		if (retval == -20096) {
			throw new RegisterationException(null, null, "company.invalid",
					HIDContants.NOTARGET, HIDContants.REQUESTNOTFOUND);
		}
		if (retval == -20097) {
			throw new RegisterationException(null, null,
					"registrationId.invalid", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}
		if (retval == 0) {
			return "success";
		} else {
			return "failure";
		}
	}

	public CallBackUrlRequest doUpdateRegisteration(long companyid,
			CallBackUrlRequest registerationRequest,String requestor) throws Exception {

		RegistrationUpdate registerationUpdate = new RegistrationUpdate(
				this.dataSource);
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("pi_requestor", requestor.trim());
		inParams.put("pio_company_id", companyid);
		inParams.put("pio_url", registerationRequest.getCallBackURL().getUrl()
				.toString().trim());
		inParams.put("pio_registration_id", registerationRequest
				.getId());
		Map<String, Object> outParams;
		outParams = registerationUpdate.execute(inParams);
		int po_retval = ((BigDecimal) outParams.get("po_retval")).intValue();
		logger.info(
				"Callback Update registration response status for the company Id [{}] from procedure  is [{}] ", companyid,
				outParams.get("po_retval").toString());
		if (po_retval == -20098) {
			throw new RegisterationException(null, null, "url.exists",
					HIDContants.MUTABILITY, HIDContants.PRECONDITIONFAILED);
		}
		if (po_retval == -20096) {
			throw new RegisterationException(null, null, "company.invalid",
					HIDContants.NOTARGET, HIDContants.REQUESTNOTFOUND);
		}

		if (po_retval == -20097) {
			throw new RegisterationException(null, null,
					"registrationId.invalid", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);
		}
		CallBackUrlRequest callBackRegisterationResponse = new CallBackUrlRequest();
		Meta meta = new Meta();
		meta.setResourceType(HIDContants.REGISTRATIONRESOURCETYPE);
		meta.setLocation(outParams.get("pio_url").toString());
		meta.setLastModified(outParams.get("po_last_modified_dt").toString());
		callBackRegisterationResponse.setMeta(meta);
		CallBackRequest callBackRequest = new CallBackRequest();
		callBackRequest.setUrl(outParams.get("pio_url").toString());
		/*callBackRequest.setId(new Long(outParams.get("pio_registration_id")
				.toString()));*/
		callBackRegisterationResponse.setCallBackURL(callBackRequest);
		callBackRegisterationResponse.setId(new Long(outParams.get("pio_registration_id")
				.toString()));
		List<String> schemaList = new ArrayList<String>();
		schemaList.add(Schemas.NOTIFICATION_REQUEST.toString());
		callBackRegisterationResponse.setSchemas(schemaList);
	
		return callBackRegisterationResponse;

	}

	public CallBackUrlRequest doGetRegisteration(long companyId,
			long registrationId) throws Exception {
		/*Object[] paramList = new Object[] { companyId, registrationId };
		int[] argTypes = new int[] { Types.NUMERIC, Types.NUMERIC };*/
		try {
			/*String resultSet = jdbcTemplateObject.queryForObject(
					HIDContants.GET_REGISTRATION_SQL, paramList, argTypes,
					String.class);*/
			
			CallBackRequest callBackRequest = (CallBackRequest)getJdbcTemplateObject().queryForObject(
					HIDContants.GET_REGISTRATION_SQL, new Object[] { companyId , registrationId}, new CallBackResponseRowMapper());
			logger.info("Callback get registration response for the company id[{}] is",companyId,callBackRequest.getUrl());
			CallBackUrlRequest callBackRegisterationResponse = new CallBackUrlRequest();
			Meta meta = new Meta();
			meta.setResourceType(HIDContants.REGISTRATIONRESOURCETYPE);
			String location = MessageFormat
					.format(resourceLocation
							.getProperty(HIDContants.CALLBACKLOCATION),
							new Long(companyId).toString()).concat(
									"/" + registrationId);
			meta.setLocation(location);
			meta.setLastModified(callBackRequest.getLastModifiedDt().toString());
			callBackRegisterationResponse.setMeta(meta);
			callBackRegisterationResponse.setId(registrationId);
			callBackRegisterationResponse.setCallBackURL(callBackRequest);
			List<String> schemaList = new ArrayList<String>();
			schemaList.add(Schemas.NOTIFICATION_REQUEST.toString());
			callBackRegisterationResponse.setSchemas(schemaList);
			return callBackRegisterationResponse;
		} catch (IndexOutOfBoundsException e) {

			throw new RegisterationException(null, null,
					"registrationId.invalid", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);

		}
		catch (EmptyResultDataAccessException e) {

			throw new RegisterationException(null, null,
					"registrationId.invalid", HIDContants.NOTARGET,
					HIDContants.REQUESTNOTFOUND);

		}
		

	}

}
