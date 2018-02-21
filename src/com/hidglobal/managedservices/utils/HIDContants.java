package com.hidglobal.managedservices.utils;

public class HIDContants {

	// HTTP Codes
	public static final int SUCCESS = 200;
	public static final int CREATED = 201;
	public static final int NORESPONSE = 204;
	public static final int INTERNALSERVERERROR = 500;
	public static final int BADREQUEST = 400;
	public static final int REQUESTNOTFOUND = 404;
	public static final int PRECONDITIONFAILED = 412;
	public static final int UNAUTHORIZED = 401;
	// DB Error Codes
	public static final int DUPLICATE_USER = -20082;
	public static final int INVALID_USER = -20081;
	public static final int DELETING_USER = -20083;
	public static final int INVALID_INVITATION = -20071;
	public static final int INVALID_PARTNUMBER = -20061;
	public static final int INVALID_MOBILEID = -20041;
	public static final int USERID_MANDATORY = -20555;
	public static final int INVALID_DEVICEID = -20031;
	public static final int INVALID_AUTOASSIGN_CREDENTIAL = -20014;
	// DB status codes
	public static final int USER_ACTIVE = 800;
	public static final int MOBILEID_ACTIVE = 5; 
	public static final int INVITATION_PENDING_STATUS = 601;
	public static final int CANCEL_INITIATED=608;
	public static final int INVITATION_CANCELLED = 605;
	public static final int INVITATION_EXPIRED = 604;
	public static final int INVITATION_ACKNOWLEDGED = 603;
	public static final int USER_DELETED = 802;
	public static final int USER_DELETE_INITIATED = 801;
	public static final int CREDENTIAL_ISSUED=753;
	public static final int REVOKE_INITIATED=754;
	public static final int ISSUE_INITIATED=750;
	// SCIM Types
	public static final String UNIQUNESS = "uniqueness";
	public static final String INVALIDSYNTAX = "invalidSyntax";
	public static final String INVALIDVALUE = "invalidValue";
	public static final String NOTARGET = "noTarget";
	public static final String INVALIDFILTER = "invalidFilter";
	public static final String MUTABILITY = "mutability";


	// HID Constants
	/*
	 * public static final String USERLOCATION =
	 * "http://127.0.0.1:8080/ma/domain/v1/users/"; public static final String
	 * INVITATIONLOCATION =
	 * "http://127.0.0.1:8080/ma/domain/v1/users/{0}/invitations/";
	 */
	public static final String DELETE_USERS_IDENTIFIER = "USER";
	public static final String DELETE_ENDPOINTS_IDENTIFIER = "ENDPOINT";
	public static final String REQUESTOR = "API_requestor";
	public static final String INTERNAL_ERROR = "Internal Server Error. Please Contact Administrator";

	// Resource URL
	public static final String USERLOCATION = "userLocation";
	public static final String CALLBACKLOCATION = "registrationLocation";
	public static final String INVITATIONLOCATION = "invitationLocation";
	public static final String PARTNUMBERLOCATION = "partNumberLocation";
	public static final String MOBILEIDLOCATION = "mobileIdLocation";
	public static final String DEVICELOCATION = "deviceIdLocation";
	public static final String RESOURCETYPELOCATION ="resourceTypesLocation";
	public static final String SERVICECONFIGLOCATION ="serviceConfigLocation";
	public static final String USERSCHEMALOCATION ="userschemaLocation";
	public static final String INVITATIONSCHEMALOCATION ="invitationschemaLocation";
	public static final String CREDENTIALCONTAINERSCHEMALOCATION ="credentialcontainerschemaLocation";
	public static final String CREDENTIALSCHEMALOCATION ="credentialschemaLocation";
	public static final String PARTNUMBERSCHEMALOCATION ="partnumberschemaLocation";
	public static final String USERACTIONSCHEMALOCATION ="useractionschemaLocation";
	public static final String VERSIONSCHEMALOCATION ="versionschemaLocation";
	public static final String REGISTRATIONSCHEMALOCATION="registrationschemaLocation";
	public static final String NOTIFICATIONSCHEMALOCATION="notificationschemaLocation";
	public static final String VERSIONLOCATION ="versionLocation";
	
	
	// Resource Type
	public static final String USERRESOURCETYPE = "PACSUser";
	public static final String INVITATIONRESOURCETYPE = "UserInvitation";
	public static final String PARTNUMBERRESOURCETYPE = "PartNumber";
	public static final String MOBILEIDRESOURCETYPE = "UserCredential";
	public static final String DEVICERESOURCETYPE= "UserCredentialContainer";
	public static final String REGISTRATIONRESOURCETYPE= "CallbackRegistration";
	public static final String VERSIONRESOURCETYPE= "SDKVersion";
	public static final String USER_ENTITY = "users";
	public static final String INVITATION_ENTITY = "invitations";
	public static final String DEVICE_ENTITY = "devices";
	public static final String MOBILE_ENTITY = "mobileids";
	public static final String PARTNUMBER_ENTITY = "partnos";

	// Entity Attribute Names
	public static final String ASCENDING = "ascending";
	public static final String DESCENDING = "descending";
	public static final String ID = "id";
	public static final String USER_STATUS_ID = "statusid";
	public static final String CREDENTIAL_STATUS_ID = "statusid";
	public static final String USER_STATUS = "status";
	public static final String FAMILY_NAME = "name.familyname";
/*	public static final String SORT_FAMILY_NAME = "name.familyname";
	public static final String SORT_GIVEN_NAME = "name.givenname";*/
	public static final String GIVEN_NAME = "name.givenname";
	public static final String STATUS = "status";
	public static final String EMAIL = "emails";
	public static final String EMAIL_VALUE = "emails.value";
	public static final String EXTERNAL_ID = "externalId";
	public static final String LAST_MODIFIED_DT = "lastModifieddt";
	public static final String DEFAULT_SORT = "LAST_MODIFIED_DT";
	public static final String COMPANY_ID = "companyid";
	public static final String ATTRIBUTES = "attributes";
	public static final String TABLE = "table";
	public static final String COMPANY_USER_ID = "companyuserid";
	public static final String INVITATION_ID = "invitationid";
	public static final String PARTNUMBER_ID = "pnheaderid";

	public static final String PART_NUMBER = "partnumber";
	public static final String PARTNUMBER_FRIENDLY_NAME = "friendlyname";
	public static final String PARTNUMBER_DESCRIPTION = "description";
	public static final String AVAILABLE_QTY = "availableqty";

	public static final String DEVICE_ENDPOINT_ID = "deviceendpointid";
	public static final String CREDENTIAL_ID = "credentialid";
	public static final String AAMK_INVITATION_ID = "aamkinvitationid";
	public static final String AAMK_INVITATION_CODE = "invitationcode";
	public static final String CREATED_TS = "createddate";
	public static final String EXPIRATION_TS = "expirationdate";

	public static final String MOBILE_ID = "credentialid";
	public static final String FRIENDLY_NAME = "friendlyname";
	public static final String CARD_NUMBER = "cardnumber";
	public static final String REFERENCE_NUMBER = "referencenumber";
	public static final String CREDENTIAL_STATUS = "status";
	public static final String CREDENTIAL_TYPE = "mobileidtype";

	public static final String SERVICE_URL = "MA_WS_URL_3_0";
	public static final String SNMP_HOST_NAME="SMTP_SERVER_HOST";
	public static final String SNMP_PORT_NO="SMTP_SERVER_PORT";
	public static final String API_VERSION="MA_API_VERSION";
	
	//SQl Queries
	
	public static final String USER_STATUS_SQL = "SELECT STATUS_ID , IS_FORCE_DELETED  FROM MA_COMPANY_USERS WHERE ID= ? AND COMPANY_ID= ?";
	public static final String GET_COMPANY_USERID_SQL="SELECT COMPANY_USER_ID,ENDPOINT_STATUS_ID FROM MA_INVITATIONS_V  WHERE ENDPOINT_ID= ? AND COMPANY_ID= ?";
	public static final String INVITATION_STATUS_SQL = "SELECT STATUS_ID FROM MA_INVITATIONS_V WHERE COMPANY_ID=? AND AAMK_INVITATION_ID = ?";
	public static final String GET_EXPIRY_MINUTES_SQL = "SELECT L.KEY,L.VALUE FROM MS_COMPANY_CONFIG_LINE L,MS_COMPANY_CONFIG_HEADER H WHERE L.COMPANY_CONFIG_HEADER_ID = H.ID AND H.PROFILE_ID = ? and key in('EMAIL_TEMPLATE_EXP_DAYS','INVITATION_EXPIRY_TIME_UNIT') ORDER BY KEY";
	public static final String CHECK_INVITATION_SQL = "SELECT STATUS_ID FROM MA_INVITATIONS_V WHERE COMPANY_USER_ID=? AND COMPANY_ID=? and USER_STATUS_ID =? and AAMK_INVITATION_ID=?";
	public static final String INITIATE_INVITATION_CANCEL_SQL = "UPDATE MA_INVITATIONS SET STATUS_ID=? WHERE AAMK_INVITATION_ID=?";
	public static final String CHECK_AVAILABLE_CREDENTIAL_SQL = "SELECT MA_INVITATION_UTILS.GET_AVAILABLE_CREDENTIAL (?,?,?,?) from dual";
	public static final String CHECK_CREDENTIAL_STATUS = "SELECT CREDENTIAL_STATUS_ID FROM MA_PART_NUMBER_CREDENTIALS_V WHERE COMPANY_ID=? AND CREDENTIAL_ID=?";
	public static final String CHECK_CREDENTIAL_USER_STATUS="SELECT CREDENTIAL_STATUS_ID FROM MA_USERS_CREDENTIALS_V WHERE COMPANY_ID=? AND CREDENTIAL_ID=?";
	public static final String GET_ENDPOINT_SQL = "SELECT ENDPOINT_ID FROM MA_INVITATIONS WHERE ID=(SELECT INVITATION_ID FROM MA_CREDENTIALS WHERE ID=? )";
	public static final String INITIATE_MOBILEID_SQL = "UPDATE MA_CREDENTIALS SET STATUS_ID=? WHERE ID=?";
	public static final String SERVICE_UTIL_SQL = "SELECT VALUE FROM MS_REGISTRY WHERE SUB_MODULE_NAME=?";
	public static final String VERSION_UTIL_SQL = "SELECT VALUE , TO_CHAR(CAST(LAST_MODIFIED_DT AS TIMESTAMP WITH TIME ZONE) AT TIME ZONE 'UTC', 'YYYY-MM-DD"+'"'+"T"+'"'+"HH24:MI:SS"+'"'+"Z"+'"'+"') LAST_MODIFIED_DT FROM MS_REGISTRY WHERE SUB_MODULE_NAME=?";
	public static final String GET_REGISTRATION_SQL="SELECT CALLBACK_URL,TO_CHAR(CAST(LAST_MODIFIED_DATE AS TIMESTAMP WITH TIME ZONE) AT TIME ZONE 'UTC', 'YYYY-MM-DD"+'"'+"T"+'"'+"HH24:MI:SS"+'"'+"Z"+'"'+"') LAST_MODIFIED_DATE FROM  MA_COMPANY_CALLBACK_URL WHERE COMPANY_ID=? AND ID=?";
	
}


