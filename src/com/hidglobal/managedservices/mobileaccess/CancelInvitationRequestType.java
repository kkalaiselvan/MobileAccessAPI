
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CancelInvitationRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CancelInvitationRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invitations" type="{http://managedservices.hidglobal.com/mobileaccess/}invitationsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CancelInvitationRequestType", propOrder = {
    "invitations"
})
@XmlRootElement(name="CancelInvitationRequest")
public class CancelInvitationRequestType {

    @XmlElement(required = true)
    protected InvitationsType invitations;

    /**
     * Gets the value of the invitations property.
     * 
     * @return
     *     possible object is
     *     {@link InvitationsType }
     *     
     */
    public InvitationsType getInvitations() {
        return invitations;
    }

    /**
     * Sets the value of the invitations property.
     * 
     * @param value
     *     allowed object is
     *     {@link InvitationsType }
     *     
     */
    public void setInvitations(InvitationsType value) {
        this.invitations = value;
    }

}
