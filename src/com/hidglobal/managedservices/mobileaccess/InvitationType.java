
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invitationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invitationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invitationId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invitationType", propOrder = {
    "invitationId"
})
@XmlRootElement(name="invitation")
public class InvitationType {

    protected long invitationId;

    /**
     * Gets the value of the invitationId property.
     * 
     */
    public long getInvitationId() {
        return invitationId;
    }

    /**
     * Sets the value of the invitationId property.
     * 
     */
    public void setInvitationId(long value) {
        this.invitationId = value;
    }

}
