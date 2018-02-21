
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateInvitationRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateInvitationRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="expiryTimeInMinutes" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateInvitationRequestType", propOrder = {
    "expiryTimeInMinutes"
})
@XmlRootElement(name="CreateInvitationRequest")
public class CreateInvitationRequestType {

    protected long expiryTimeInMinutes;

    /**
     * Gets the value of the expiryTimeInMinutes property.
     * 
     */
    public long getExpiryTimeInMinutes() {
        return expiryTimeInMinutes;
    }

    /**
     * Sets the value of the expiryTimeInMinutes property.
     * 
     */
    public void setExpiryTimeInMinutes(long value) {
        this.expiryTimeInMinutes = value;
    }

}
