
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CreateInvitationResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateInvitationResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invitationId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="invitationCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://managedservices.hidglobal.com/mobileaccess/}invitationStatusType"/>
 *         &lt;element name="createdAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="expiresAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateInvitationResponseType", propOrder = {
    "invitationId",
    "invitationCode",
    "status",
    "createdAt",
    "expiresAt"
})
@XmlRootElement(name="CreateInvitationResponse")
public class CreateInvitationResponseType {

    protected long invitationId;
    @XmlElement(required = true)
    protected String invitationCode;
    @XmlElement(required = true)
    protected InvitationStatusType status;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdAt;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expiresAt;

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

    /**
     * Gets the value of the invitationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvitationCode() {
        return invitationCode;
    }

    /**
     * Sets the value of the invitationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvitationCode(String value) {
        this.invitationCode = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link InvitationStatusType }
     *     
     */
    public InvitationStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link InvitationStatusType }
     *     
     */
    public void setStatus(InvitationStatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the createdAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the value of the createdAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedAt(XMLGregorianCalendar value) {
        this.createdAt = value;
    }

    /**
     * Gets the value of the expiresAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the value of the expiresAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiresAt(XMLGregorianCalendar value) {
        this.expiresAt = value;
    }

}
