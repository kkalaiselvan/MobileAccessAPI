package com.hidglobal.managedservices.vo;

public class Schema {

	public enum Schemas {

		PACSUSER("urn:ietf:params:scim:schemas:core:2.0:User"), PACS_USER_ACTIONS(
				"urn:hid:scim:api:ma:1.0:UserAction"), INVITATIONS(
				"urn:hid:scim:api:ma:1.0:UserInvitation"), DEVICES(
				"urn:hid:scim:api:ma:1.0:CredentialContainer"), PART_NUMBER(
				"urn:hid:scim:api:ma:1.0:PartNumber"), MOBILE_ID(
				"urn:hid:scim:api:ma:1.0:Credential"), DEVICE_MOBILEID(
				"urn:hid:scim:api:ma:1.0:UserMobileIds"), ACTIVE_MOBILEID(
				"urn:hid:scim:api:ma:1.0:ActiveMobileIds"), LIST_RESPONSE(
				"urn:ietf:params:scim:api:messages:2.0:ListResponse"), SEARCH_REQUEST(
				"urn:ietf:params:scim:api:messages:2.0:SearchRequest"), ERROR(
				"urn:hid:scim:api:ma:1.0:Error"),NOTIFICATION_REQUEST("urn:hid:scim:api:ma:1.0:CallbackRegistration"),
				VERSION_REQUEST("urn:hid:scim:api:ma:1.0:SDKVersion");

		String schema;

		Schemas(String schema) {

			this.schema = schema;
		}

		@Override
		public String toString() {
			return schema;
		}

	}

}
