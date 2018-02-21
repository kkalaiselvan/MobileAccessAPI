package com.hidglobal.managedservices.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.RegisterationException;
import com.hidglobal.managedservices.exception.UserException;
import com.hidglobal.managedservices.exception.IssueCredentialException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.FilterParserException;
import com.hidglobal.managedservices.exception.MobileIdException;
import com.hidglobal.managedservices.vo.ErrorDetails;
import com.hidglobal.managedservices.vo.Schema.Schemas;

@Component
public class ErrorHelper {

	@Autowired
	MessageSource messageSource;
	ErrorDetails errorDetails;

	@Autowired
	@Qualifier("dbColumnMap")
	Properties dbColumnMap;

	@Autowired
	@Qualifier("sqlCodesHttpCodesMap")
	Properties sqlCodesHttpCodesMap;
	private static final Logger logger = LoggerFactory.getLogger(ErrorHelper.class);

	
	public Properties getSqlCodesHttpCodesMap() {
		return sqlCodesHttpCodesMap;
	}

	public void setSqlCodesHttpCodesMap(Properties sqlCodesHttpCodesMap) {
		this.sqlCodesHttpCodesMap = sqlCodesHttpCodesMap;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public Response setErrorResponse(String errorCode, String detail,
			String errorData, String scimType, int httpStatus) {

		errorDetails = new ErrorDetails();

		logger.error("Creating Error Response with Error Message: " + detail
				+ " Error Code: " + errorCode
				+ " Error Data to be incorporated: " + errorData
				+ " scimType: " + scimType + " httpStatus: " + httpStatus);

		if (detail == null) {
			System.out.println("Detail is null");
			if (errorData != null) {
				System.out.println("Setting error Data");
				detail = messageSource.getMessage(errorCode,
						new Object[] { errorData }, Locale.US);
			} else {
				detail = messageSource.getMessage(errorCode, null, Locale.US);
			}

			logger.error("Error Message from Property File is [{}]", errorData);
		}

		if (httpStatus == 0) {
			httpStatus = Integer
					.parseInt(sqlCodesHttpCodesMap.getProperty(errorCode,
							String.valueOf(HIDContants.INTERNALSERVERERROR)));
		}

		errorDetails.setStatus(httpStatus);
		errorDetails.setDetail(detail);
		errorDetails.setScimType(scimType);
		setErrorSchema();

		return Response.status(httpStatus).entity(errorDetails).build();

	}

	public Response setErrorResponse(String exception) {
		errorDetails = new ErrorDetails();
		errorDetails.setStatus(HIDContants.INTERNALSERVERERROR);
		errorDetails.setDetail(messageSource.getMessage(
				String.valueOf(Response.Status.INTERNAL_SERVER_ERROR
						.getStatusCode()), null, Locale.US));
		setErrorSchema();
		return Response.status(HIDContants.INTERNALSERVERERROR)
				.entity(errorDetails).build();

	}

	public void setErrorSchema() {
		List<String> schema = new ArrayList<String>();
		schema.add(Schemas.ERROR.toString());
		errorDetails.setSchemas(schema);

	}

	public Response handleException(Exception ex) {
		Response response;
		if (ex instanceof MAValidationException) {
			logger.error("Handling MAValidation Exception");
			MAValidationException validationException = (MAValidationException) ex;
			return (setErrorResponse(validationException.getErrCode(),
					validationException.getDetail(),
					validationException.getErrorData(),
					validationException.getScimType(),
					validationException.getHttpStatus()));

		}

		else if (ex instanceof DBException) {
			logger.error("Handling Database Exception");
			DBException insertFailedException = (DBException) ex;
			return (setErrorResponse(insertFailedException.getErrCode(), null,
					insertFailedException.getErrorData(),
					insertFailedException.getScimType(), 0));

		} else if ((ex instanceof WebServiceException)
				|| ex instanceof SQLException) {
			logger.error("Handling Webservice Exception");
			return (setErrorResponse(null, "Internal Server Error", null,
					HIDContants.INTERNAL_ERROR, HIDContants.INTERNALSERVERERROR));

		} else if (ex instanceof InvitationException) {
			logger.error("Handling Invitation Exception");
			InvitationException e = (InvitationException) ex;
			return (setErrorResponse(e.getErrCode(), ex.getMessage(),
					e.getErrorData(), e.getScimType(), e.getHttpStatus()));

		} else if (ex instanceof IssueCredentialException) {
			logger.error("Handling Invitation Exception");
			IssueCredentialException e = (IssueCredentialException) ex;
			return (setErrorResponse(e.getErrCode(), ex.getMessage(),
					e.getErrorData(), e.getScimType(), e.getHttpStatus()));

		} else if (ex instanceof UserException) {
			logger.error("Handling Delete User Exception");
			UserException e = (UserException) ex;
			return (setErrorResponse(e.getErrCode(), e.getMessage(),
					e.getErrorData(), e.getScimType(), e.getHttpStatus()));

		} else if (ex instanceof FilterParserException) {
			logger.error("Handling Filter Parser Exception");
			return setErrorResponse(null,
					((FilterParserException) ex).getErrorDetail(), null,
					((FilterParserException) ex).getScimType(), HIDContants.BADREQUEST);

		} else if (ex instanceof MobileIdException) {
			logger.error("Handling MobileId Exception");
			MobileIdException e = (MobileIdException) ex;
			response = setErrorResponse(e.getErrCode(), ex.getMessage(),
					e.getErrorData(), e.getScimType(), e.getHttpStatus());
			return response;

		}
		else if (ex instanceof RegisterationException) {
			/*logger.error("Handling Registeration Exception");
			RegisterationException e = (RegisterationException) ex;
			return (setErrorResponse(e.getErrCode(), ex.getMessage(),
					e.getErrorData(), e.getScimType(), e.getHttpStatus()));*/
			
			logger.error("Handling Registration Exception");
			RegisterationException e = (RegisterationException) ex;
			return (setErrorResponse(e.getErrCode(), ex.getMessage(),
					e.getErrorData(), e.getScimType(), e.getHttpStatus()));

		}

		else if (ex instanceof DataIntegrityViolationException) {

			Throwable e = ((DataIntegrityViolationException) ex)
					.getMostSpecificCause();

			if (e instanceof SQLException
					&& ((SQLException) e).getErrorCode() == 1722) {

				System.out.println(((SQLException) e).getErrorCode());
				response = setErrorResponse("invalid.filter.number", null,
						null, "invalidValue", HIDContants.BADREQUEST);
				return response;
			} else {
				response = setErrorResponse(ex.getMessage());
				return response;
			}

		}

		else if (ex instanceof JsonProcessingException) {
			logger.error("Handling JSON Parser Exception");
			return setErrorResponse(null, ex.getMessage(), null, null, HIDContants.BADREQUEST);

		} else {
			return (setErrorResponse(ex.getMessage()));

		}

	}

}
