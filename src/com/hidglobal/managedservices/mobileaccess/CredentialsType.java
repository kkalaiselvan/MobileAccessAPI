
package com.hidglobal.managedservices.mobileaccess;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for credentialsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="credentialsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credential" type="{http://managedservices.hidglobal.com/mobileaccess/}credentialType" maxOccurs="50"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credentialsType", propOrder = {
    "credential"
})
@XmlRootElement(name="credentials")
public class CredentialsType {

    @XmlElement(required = true)
    protected List<CredentialType> credential;

    /**
     * Gets the value of the credential property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the credential property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCredential().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CredentialType }
     * 
     * 
     */
    public List<CredentialType> getCredential() {
        if (credential == null) {
            credential = new ArrayList<CredentialType>();
        }
        return this.credential;
    }

}
