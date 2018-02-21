
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for credentialType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="credentialType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credentialId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="endpointId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credentialType", propOrder = {
    "credentialId",
    "endpointId"
})
@XmlRootElement(name="credential")
public class CredentialType {

    protected long credentialId;
    protected long endpointId;

    /**
     * Gets the value of the credentialId property.
     * 
     */
    public long getCredentialId() {
        return credentialId;
    }

    /**
     * Sets the value of the credentialId property.
     * 
     */
    public void setCredentialId(long value) {
        this.credentialId = value;
    }

    /**
     * Gets the value of the endpointId property.
     * 
     */
    public long getEndpointId() {
        return endpointId;
    }

    /**
     * Sets the value of the endpointId property.
     * 
     */
    public void setEndpointId(long value) {
        this.endpointId = value;
    }

}
