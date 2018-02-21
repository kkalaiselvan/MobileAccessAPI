package com.hidglobal.managedservices.validator;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.exception.FilterParserException;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.exception.RegisterationException;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.vo.CallBackRequest;
import com.hidglobal.managedservices.vo.CallBackUrlRequest;
import com.hidglobal.managedservices.vo.PacsInvitation;
import com.hidglobal.managedservices.vo.PacsUser;
import com.hidglobal.managedservices.vo.PacsUserActions;
import com.hidglobal.managedservices.vo.Schema;
import com.hidglobal.managedservices.vo.Schema.Schemas;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.UserMobileAction;

/**
 * 
 * MAValidator performs Field Level Validations for Mobile Access Entities.
 * 
 */
@Component
public class MAValidator {

	private Validator validator;
	private static final Logger logger = LoggerFactory
			.getLogger(MAValidator.class);
	private static final String SORT = "sort";
	private static final String ATTR = "attribute";

	@Autowired
	@Qualifier("filterAttributes")
	private Properties filterAttribute;

	public MAValidator() {

		ValidatorFactory validatorFactory = Validation
				.byDefaultProvider()
				.configure()
				.messageInterpolator(
						new ResourceBundleMessageInterpolator(
								(ResourceBundleLocator) new PlatformResourceBundleLocator(
										"messages_en")))
				.buildValidatorFactory();

		validator = validatorFactory.getValidator();
	}

	/**
	 * This method performs validation on PACS user input.
	 * 
	 * @param user
	 *            PACS user object that needs to be validated.
	 * @throws MAValidationException
	 *             Any violations in input validation are thrown as
	 *             MAVAlidationException
	 */

	public void checkUserViolations(PacsUser user) throws MAValidationException {

		Set<ConstraintViolation<PacsUser>> violations = validator
				.validate(user);

		for (ConstraintViolation<PacsUser> violation : violations) {

			logger.error(
					"Validation Exception while Enrolling User. Exception Message: [{}]",
					violation.getMessage());
			throw new MAValidationException(null, violation.getMessage(), null,
					HIDContants.INVALIDSYNTAX, HIDContants.BADREQUEST);
		}

	}

	public void checkRegistrationViolations(CallBackRequest registrationRequest)
			throws MAValidationException {

		Set<ConstraintViolation<CallBackRequest>> violations = validator
				.validate(registrationRequest);

		for (ConstraintViolation<CallBackRequest> violation : violations) {

			logger.error(
					"Validation Exception while Registring CallBack URL. Exception Message: [{}]",
					violation.getMessage());
			throw new MAValidationException(null, violation.getMessage(), null,
					HIDContants.INVALIDSYNTAX, HIDContants.BADREQUEST);
		}

	}

	/**
	 * This method validates the PACS User Actions.
	 * 
	 * @param pacsUserAction
	 * @throws InvitationException
	 */
	public void validateInvitationActions(PacsUserActions pacsUserAction)
			throws InvitationException {

		if (pacsUserAction != null) {

			if ((pacsUserAction.getSendInvitationEmail() != null)
					&& !((("y".equalsIgnoreCase(pacsUserAction
							.getSendInvitationEmail().toString().trim())))
							|| ("n".equalsIgnoreCase(pacsUserAction
									.getSendInvitationEmail().toString().trim())) || (pacsUserAction
								.getSendInvitationEmail().isEmpty()))) {

				throw new InvitationException(
						"This Request cannot be performed for this send Invitation Value",
						null, "invalid.userAction", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}

			if ((pacsUserAction.getAssignMobileId() != null)
					&& !((("y".equalsIgnoreCase(pacsUserAction
							.getAssignMobileId().toString().trim())))
							|| ("n".equalsIgnoreCase(pacsUserAction
									.getAssignMobileId().toString().trim())) || (pacsUserAction
								.getAssignMobileId().isEmpty()))) {

				throw new InvitationException(
						"This Request cannot be performed for this AssignMobileId Value",
						null, "invalid.userAction", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}
		}
	}

	/**
	 * This method validates PACS User inputs for enroll user.
	 * 
	 * @param user
	 *            PACS User object that needs to be validated
	 * @throws MAValidationException
	 */
	public void validateUserforInsert(PacsUser user)
			throws MAValidationException {

		checkUserViolations(user);
		validateUserSchema(user);
	}

	/**
	 * This method performs PACS User schema validation during Enroll User
	 * Operation.
	 * 
	 * @param user
	 *            PACS User object which has schema element
	 * @throws MAValidationException
	 */
	public void validateUserSchema(PacsUser user) throws MAValidationException {
		// TODO Auto-generated method stub

		if (user.getSchemas() != null && user.getSchemas().size() == 2) {
			if (user.getSchemas().get(0).equals(user.getSchemas().get(1))) {
				throw new MAValidationException("Dublicate ", null,
						"invalid.schema", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}
			for (String schemaValue : user.getSchemas()) {
				if (schemaValue.equals(Schemas.PACSUSER.toString())) {
				} else if (schemaValue.equals(Schemas.PACS_USER_ACTIONS
						.toString())) {
				}

				else {
					throw new MAValidationException(schemaValue, null,
							"invalid.schema", HIDContants.INVALIDVALUE,
							HIDContants.BADREQUEST);
				}

			}

		} else if (user.getSchemas() != null && user.getSchemas().size() == 1) {

			if (user.getSchemas().get(0).equals(Schemas.PACSUSER.toString())) {

			} else {
				throw new MAValidationException(user.getSchemas().get(0)
						.toString(), null, "invalid.schema",
						HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
			}

		}

		else {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}

		if (user.getSchemas() != null && user.getSchemas().size() == 1
				&& user.getUserActions() != null) {

			throw new MAValidationException("User Action", null,
					"invalid.userAction", HIDContants.INVALIDVALUE,
					HIDContants.BADREQUEST);
		}

		/*
		 * if (user.getMeta() != null && user.getMeta().getResourceType() !=
		 * null) { if
		 * (!(HIDContants.USERRESOURCETYPE.equals(user.getMeta().getResourceType
		 * ()))) { throw new
		 * MAValidationException(user.getMeta().getResourceType(), null,
		 * "invalid.meta", HIDContants.INVALIDVALUE, HIDContants.BADREQUEST); }
		 * }
		 */

	}

	/**
	 * This method performs PACS User schema validation during Update User
	 * Operation.
	 * 
	 * @param user
	 *            PACS User object which has schema element
	 * @throws MAValidationException
	 */
	public void validateUpdateUserSchema(PacsUser user)
			throws MAValidationException {
		// TODO Auto-generated method stub

		if (user.getSchemas() != null && user.getSchemas().size() == 1) {
			if (user.getSchemas().get(0).toString()
					.equals(Schemas.PACSUSER.toString())) {

			} else {
				throw new MAValidationException(user.getSchemas().get(0)
						.toString(), null, "invalid.schema",
						HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
			}
		} else {
			throw new MAValidationException(null, null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
	}

	/**
	 * This method performs Validation for any Search Request.It validates the
	 * entity attributes are valid.
	 * 
	 * @param request
	 *            SearchRequest that needs to be validated
	 * @param schema
	 *            Schema which identifies the entity type.
	 * @throws MAValidationException
	 * @throws FilterParserException
	 */
	public void validateSearchRequest(SearchRequest request, Schemas schema)
			throws MAValidationException, FilterParserException {

		validateSearchSchema(request);
		if (request.getAttributes() != null) {
			for (int i = 0; i < request.getAttributes().size(); i++) {
				request.getAttributes().set(
						i,
						validateSchemaPrefix(request.getAttributes().get(i),
								schema));
			}
		}

		if (request.getFilterList() != null)
			validateFilter(request.getFilterList());
		validateSortOrder(request.getSortingOrder());

		if (schema == Schemas.PACSUSER) {
			validateUserAttributes(request.getAttributes(), ATTR);
			if (!(request.getSortBy() == null || request.getSortBy().isEmpty())) {
				request.setSortBy(validateSchemaPrefix(request.getSortBy(),
						schema));
				validateUserAttribute(request.getSortBy(), SORT);

			}
		}

		if (schema == Schemas.INVITATIONS) {

			validateInvitationAttributes(request.getAttributes(), ATTR);
			if (!(request.getSortBy() == null || request.getSortBy().isEmpty())) {
				request.setSortBy(validateSchemaPrefix(request.getSortBy(),
						schema));
				validateInvitationAttribute(request.getSortBy(), SORT);
			}
		}
		if (schema == Schemas.PART_NUMBER) {

			validatePartNoAttributes(request.getAttributes(), ATTR);
			if (!(request.getSortBy() == null || request.getSortBy().isEmpty())) {
				request.setSortBy(validateSchemaPrefix(request.getSortBy(),
						schema));
				validatePartNumberAttribute(request.getSortBy(), SORT);
			}
		}
		if (schema == Schemas.MOBILE_ID) {

			validateMobileIdAttributes(request.getAttributes(), ATTR);
			if (!(request.getSortBy() == null || request.getSortBy().isEmpty())) {
				request.setSortBy(validateSchemaPrefix(request.getSortBy(),
						schema));
				validateMobileIdAttribute(request.getSortBy(), SORT);
			}
		}

	}

	/**
	 * This method validates the schema for Search Operations.
	 * 
	 * @param request
	 *            SearchRequest that contains the schema attribute to be
	 *            validated.
	 * @throws MAValidationException
	 */
	private void validateSearchSchema(SearchRequest request)
			throws MAValidationException {
		// TODO Auto-generated method stub
		if (request.getSchemas() != null && request.getSchemas().size() == 1) {
			if (request.getSchemas().get(0).toString()
					.equals(Schemas.SEARCH_REQUEST.toString())) {

			} else {
				throw new MAValidationException(request.getSchemas().get(0)
						.toString(), null, "invalid.schema",
						HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
			}
		} else {
			throw new MAValidationException(null, null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}

	}

	/**
	 * This method validates PACS User inputs for update user operation.
	 * 
	 * @param user
	 *            PACS User object that needs to be validated
	 * @throws MAValidationException
	 */

	public void validateUserforUpdate(PacsUser user)
			throws MAValidationException {
		checkUserViolations(user);
		validateUpdateUserSchema(user);

		if ((user.getId()) == 0L) {

			throw new MAValidationException(null, null,
					"invalid.companyUserId", HIDContants.INVALIDVALUE,
					HIDContants.BADREQUEST);
		}
	}

	private void validateUserAttributes(List<String> attributes, String flag)
			throws MAValidationException {

		if (attributes != null) {
			for (String attribute : attributes) {
				System.out.println("Validating User Attributes: " + attribute);

				validateUserAttribute(attribute, flag);
			}
		}
	}

	private void validateUserAttribute(String attribute, String flag)
			throws MAValidationException {
		if (!(attribute.equalsIgnoreCase(HIDContants.FAMILY_NAME)
				|| attribute.equalsIgnoreCase(HIDContants.GIVEN_NAME)
				|| attribute.equalsIgnoreCase(HIDContants.EMAIL)
				|| attribute.equalsIgnoreCase(HIDContants.EMAIL_VALUE))) {

			if (flag.equals("sort")) {
				logger.error(
						"Found Invalid Sort By Attribute [{}] in User Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.sortBy", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			} else {
				logger.error(
						"Found Invalid  Attribute [{}] in User Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.attribute", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			}
		}
	}

	/**
	 * This method validates the search filter in SearchRequest
	 * 
	 * @param filters
	 *            Filter string in SearchRequest
	 * @throws MAValidationException
	 *             throws MAValidationException when filter validation against
	 *             SCIM standards fails.
	 */
	public void validateFilter(String filters) throws MAValidationException {
		if (filters.isEmpty()) {
			logger.error("Filter Should be a valid Boolean");
			throw new MAValidationException(filters, null, "invalid.filter",
					HIDContants.INVALIDFILTER, HIDContants.BADREQUEST);
		}
	}

	/**
	 * This method validates the Sorting Order values in SearchRequest.
	 * 
	 * @param attribute
	 *            Sort Order value in SearchRequest
	 * @throws MAValidationException
	 */
	private void validateSortOrder(String attribute)
			throws MAValidationException {

		if (attribute != null) {

			if (!(attribute.isEmpty()
					|| attribute.equalsIgnoreCase(HIDContants.ASCENDING) || attribute
						.equalsIgnoreCase(HIDContants.DESCENDING))) {
				logger.error("Found Invalid Sort Order [{}] in Search Operation: "
						+ attribute);
				throw new MAValidationException(attribute, null,
						"invalid.sortOrder", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);

			}
		}
	}

	/**
	 * This method validates the User Actions for Create Invitation Operation.
	 * 
	 * @param attribute
	 *            Sort Order value in SearchRequest
	 * @throws MAValidationException
	 */
	public void validateInvitationUserActions(PacsUserActions pacsUserAction)
			throws InvitationException {
		if (pacsUserAction != null) {

			if ((pacsUserAction.getCreateInvitationCode() != null)
					&& !((("y".equalsIgnoreCase(pacsUserAction
							.getCreateInvitationCode().toString().trim())))
							|| ("n".equalsIgnoreCase(pacsUserAction
									.getCreateInvitationCode().toString()
									.trim())) || (pacsUserAction
							.getCreateInvitationCode().toString().trim()
								.isEmpty()))) {

				throw new InvitationException(
						"This Request cannot be performed for this create Invitation Value",
						null, "invalid.userAction", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}

			if ((pacsUserAction.getCreateInvitationCode() != null)
					&& ("y".equalsIgnoreCase(pacsUserAction
							.getCreateInvitationCode().toString()))
					&& (pacsUserAction.getSendInvitationEmail() != null)
					&& !((("y".equalsIgnoreCase(pacsUserAction
							.getSendInvitationEmail().toString().trim())))
							|| ("n".equalsIgnoreCase(pacsUserAction
									.getSendInvitationEmail().toString().trim())) || (pacsUserAction
								.getSendInvitationEmail().isEmpty()))) {

				throw new InvitationException(
						"This Request cannot be performed for this send Invitation Value",
						null, "invalid.userAction", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}

			if ((pacsUserAction.getCreateInvitationCode() != null)
					&& (pacsUserAction.getAssignMobileId() != null)
					&& ("y".equalsIgnoreCase(pacsUserAction
							.getCreateInvitationCode().toString()))
					&& !((("y".equalsIgnoreCase(pacsUserAction
							.getAssignMobileId().toString().trim())))
							|| ("n".equalsIgnoreCase(pacsUserAction
									.getAssignMobileId().toString().trim())) || (pacsUserAction
								.getAssignMobileId().isEmpty()))) {

				throw new InvitationException(
						"This Request cannot be performed for this AssignMobileId Value",
						null, "invalid.userAction", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}

		}

	}

	public void validateInvitationSchema(PacsInvitation invitation)

	throws MAValidationException {
		if (invitation.getSchemas() == null) {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (invitation.getSchemas().size() == 0) {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (invitation.getSchemas().size() > 1) {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (invitation.getSchemas() != null
				&& invitation.getSchemas().size() > 0) {
			if (!(Schemas.PACS_USER_ACTIONS.toString().equals(invitation
					.getSchemas().get(0).toString()))) {
				throw new MAValidationException(invitation.getSchemas().get(0)
						.toString(), null, "invalid.schema",
						HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
			}
		}

	}

	private void validateInvitationAttributes(List<String> attributes,
			String flag) throws MAValidationException {

		if (attributes != null) {
			for (String attribute : attributes) {

				validateInvitationAttribute(attribute, flag);

			}
		}
	}

	private void validateInvitationAttribute(String attribute, String flag)
			throws MAValidationException {
		if (!(attribute.equalsIgnoreCase(HIDContants.ID)
				|| attribute.equalsIgnoreCase(HIDContants.AAMK_INVITATION_CODE)
				|| attribute.equalsIgnoreCase(HIDContants.AAMK_INVITATION_ID) || attribute
					.equalsIgnoreCase(HIDContants.STATUS))) {
			if (flag.equals(SORT)) {
				logger.error(
						"Found Invalid Sort By Attribute [{}] in Invitation Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.sortBy", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			} else {
				logger.error(
						"Found Invalid  Attribute [{}] in Invitation Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.attribute", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			}
		}
	}

	/**
	 * This method validates the Part Number Attributes for Search Part Number
	 * Operation.
	 * 
	 * @param attributes
	 *            SearchRequest Object which has the query attributes
	 * @throws MAValidationException
	 */
	private void validatePartNoAttributes(List<String> attributes, String flag)
			throws MAValidationException {
		// TODO Auto-generated method stub

		if (attributes != null) {
			for (String attribute : attributes) {

				validatePartNumberAttribute(attribute, flag);
			}
		}
	}

	private void validatePartNumberAttribute(String attribute, String flag)
			throws MAValidationException {
		if (!(attribute.equalsIgnoreCase(HIDContants.ID)
				|| attribute.equalsIgnoreCase(HIDContants.PART_NUMBER)
				|| attribute
						.equalsIgnoreCase(HIDContants.PARTNUMBER_FRIENDLY_NAME)
				|| attribute
						.equalsIgnoreCase(HIDContants.PARTNUMBER_DESCRIPTION) || attribute
					.equalsIgnoreCase(HIDContants.AVAILABLE_QTY))) {
			if (flag.equals(SORT)) {
				logger.error(
						"Found Invalid Sort By Attribute [{}] in PartNo Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.sortBy", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			} else {
				logger.error(
						"Found Invalid  Attribute [{}] in PartNo Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.attribute", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			}
		}
	}

	/**
	 * This method validates the Part Number Attributes for Search Mobile Id
	 * Operation Operation.
	 * 
	 * @param attributes
	 *            SearchRequest Object which has the query attributes
	 * @throws MAValidationException
	 */
	private void validateMobileIdAttributes(List<String> attributes, String flag)
			throws MAValidationException {
		// TODO Auto-generated method stub

		if (attributes != null) {
			for (String attribute : attributes) {

				validateMobileIdAttribute(attribute, flag);
			}
		}
	}

	private void validateMobileIdAttribute(String attribute, String flag)
			throws MAValidationException {
		if (!(attribute.equalsIgnoreCase(HIDContants.ID)
				|| attribute.equalsIgnoreCase(HIDContants.PART_NUMBER)
				|| attribute.equalsIgnoreCase(HIDContants.FRIENDLY_NAME)
				|| attribute.equalsIgnoreCase(HIDContants.CARD_NUMBER)
				|| attribute.equalsIgnoreCase(HIDContants.REFERENCE_NUMBER) || attribute
					.equalsIgnoreCase(HIDContants.CREDENTIAL_STATUS))) {
			if (flag.equals(SORT)) {
				logger.error(
						"Found Invalid Sort By Attribute [{}] in MobileId Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.sortBy", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			} else {
				logger.error(
						"Found Invalid  Attribute [{}] in MobileId Search Operation: ",
						attribute);
				throw new MAValidationException(attribute, null,
						"invalid.attribute", HIDContants.INVALIDFILTER,
						HIDContants.BADREQUEST);
			}
		}
	}

	/**
	 * This method validates the schema prefixes in attribute values.
	 * 
	 * @param word
	 *            Attribute values in request.
	 * @param schema
	 *            Schema to identify the entity
	 * @return
	 * @throws FilterParserException
	 */

	private static String validateSchemaPrefix(String word,
			Schema.Schemas schema) throws FilterParserException {

		int schemaSeparatorPos = word.lastIndexOf(":");

		if (schemaSeparatorPos > 0) {
			String attributeSchema = word.substring(0, schemaSeparatorPos);

			if (attributeSchema.equalsIgnoreCase(schema.toString())) {
				word = word.substring(schemaSeparatorPos + 1);
			} else {
				throw new FilterParserException(
						String.format(
								"Exception while parsing search request, schema %s is not correct",
								attributeSchema), HIDContants.INVALIDSYNTAX,
						Response.Status.BAD_REQUEST.getStatusCode());
			}
		}
		return word;
	}

	public void validateIssueCredential(UserMobileAction userMobileAction)
			throws MAValidationException {
		if (userMobileAction.getSchemas() == null) {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (userMobileAction.getSchemas() != null
				&& userMobileAction.getSchemas().size() > 0) {
			if (!(Schemas.PACS_USER_ACTIONS.toString().equals(userMobileAction
					.getSchemas().get(0).toString()))) {
				throw new MAValidationException(userMobileAction.getSchemas()
						.get(0).toString(), null, "invalid.schema",
						HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
			}

		}
	}

	public void validateRegistrationSchema(
			CallBackUrlRequest registrationRequest)
			throws MAValidationException, RegisterationException {
		if (registrationRequest == null) {

			throw new RegisterationException(null, null,
					"invalid.url.required", HIDContants.INVALIDVALUE,
					HIDContants.BADREQUEST);
		}

		if (registrationRequest.getSchemas() == null) {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (registrationRequest.getSchemas().size() == 0) {
			throw new MAValidationException("request", null, "invalid.schema",
					HIDContants.INVALIDVALUE, HIDContants.BADREQUEST);
		}
		if (registrationRequest.getSchemas() != null
				&& registrationRequest.getSchemas().size() > 0) {
			if (!(Schemas.NOTIFICATION_REQUEST.toString()
					.equals(registrationRequest.getSchemas().get(0).toString()))) {
				throw new MAValidationException(registrationRequest
						.getSchemas().get(0).toString(), null,
						"invalid.schema", HIDContants.INVALIDVALUE,
						HIDContants.BADREQUEST);
			}
			if (registrationRequest != null) {

				if (registrationRequest.getCallBackURL() == null) {

					throw new RegisterationException(null, null,
							"invalid.url.required", HIDContants.INVALIDVALUE,
							HIDContants.BADREQUEST);
				}
			}
		}

	}

	public void validateSearchUserAttribute(SearchRequest request)
			throws MAValidationException, FilterParserException {
		validateSearchSchema(request);
		
		if (request != null && request.getAttributes() != null) {
			for (int i = 0; i < request.getAttributes().size(); i++) {
				if (!(request.getAttributes().get(i).toLowerCase()
						.equals(filterAttribute.getProperty("fname")
								.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemafname").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemagname").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty("gname")
										.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty("email")
										.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty("emails")
										.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemaemail").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemaemails").toLowerCase()))) {
					throw new MAValidationException(request.getAttributes().get(i)
							.toLowerCase(), null,
							"invalid.attribute", HIDContants.INVALIDFILTER,
							HIDContants.BADREQUEST);
					/*throw new FilterParserException(
							String.format(
									"Exception while parsing filter, attribute name %s is not supported.",
									request.getAttributes().get(i)
											.toLowerCase()),
							HIDContants.INVALIDSYNTAX,
							Response.Status.BAD_REQUEST.getStatusCode());*/
				}
			}

		}
	}

	public void validateSearchInvitationAttribute(SearchRequest request)
			throws MAValidationException, FilterParserException {
		if (request != null && request.getAttributes() != null) {
			for (int i = 0; i < request.getAttributes().size(); i++) {
				if (!(request.getAttributes().get(i).toLowerCase()
						.equals(filterAttribute.getProperty("invitationid")
								.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemainvitationid").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"invitationcode").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemainvitationcode").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"invitationstatus").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemainvitationstatus").toLowerCase()))) {
					throw new FilterParserException(
							String.format(
									"Exception while parsing filter, attribute name %s is not supported.",
									request.getAttributes().get(i)
											.toLowerCase()),
							HIDContants.INVALIDSYNTAX,
							Response.Status.BAD_REQUEST.getStatusCode());
				}
			}

		}
	}

	public void validateSearchPartNumberAttribute(SearchRequest request)
			throws MAValidationException, FilterParserException {
		if (request != null && request.getAttributes() != null) {
			for (int i = 0; i < request.getAttributes().size(); i++) {
				if (!(request.getAttributes().get(i).toLowerCase()
						.equals(filterAttribute.getProperty("partnumberid")
								.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemapartnumberid").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"partnumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemapartnumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"friendlyname").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemafriendlyname").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"description").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemadescription").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"availableqty").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemaavailableqty").toLowerCase()))) {
					throw new FilterParserException(
							String.format(
									"Exception while parsing filter, attribute name %s is not supported.",
									request.getAttributes().get(i)
											.toLowerCase()),
							HIDContants.INVALIDSYNTAX,
							Response.Status.BAD_REQUEST.getStatusCode());
				}
			}

		}
	}

	public void validateSearchCredentialAttribute(SearchRequest request)
			throws MAValidationException, FilterParserException {
		if (request != null && request.getAttributes() != null) {
			for (int i = 0; i < request.getAttributes().size(); i++) {
				if (!(request.getAttributes().get(i).toLowerCase()
						.equals(filterAttribute.getProperty("partnumber")
								.toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemapartnumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"credentialid").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemacredentialid").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"cardnumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemacardnumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"referencenumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemareferencenumber").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"credentialstatus").toLowerCase()))
						&& !(request.getAttributes().get(i).toLowerCase()
								.equals(filterAttribute.getProperty(
										"schemacredentialstatus").toLowerCase()))) {

					throw new FilterParserException(
							String.format(
									"Exception while parsing filter, attribute name %s is not supported.",
									request.getAttributes().get(i)
											.toLowerCase()),
							HIDContants.INVALIDSYNTAX,
							Response.Status.BAD_REQUEST.getStatusCode());
				}
			}

		}
	}

}