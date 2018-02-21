
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invitationStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="invitationStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PENDING"/>
 *     &lt;enumeration value="ACKNOWLEDGED"/>
 *     &lt;enumeration value="EXPIRED"/>
 *     &lt;enumeration value="NOT_SUPPORTED"/>
 *     &lt;enumeration value="NOT_ELIGIBLE"/>
 *     &lt;enumeration value="CANCELLED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "invitationStatusType")
@XmlEnum
public enum InvitationStatusType {

    PENDING,
    ACKNOWLEDGED,
    EXPIRED,
    NOT_SUPPORTED,
    NOT_ELIGIBLE,
    CANCELLED;

    public String value() {
        return name();
    }

    public static InvitationStatusType fromValue(String v) {
        return valueOf(v);
    }

}
