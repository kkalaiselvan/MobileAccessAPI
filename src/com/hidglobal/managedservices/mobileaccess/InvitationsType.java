
package com.hidglobal.managedservices.mobileaccess;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invitationsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invitationsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invitation" type="{http://managedservices.hidglobal.com/mobileaccess/}invitationType" maxOccurs="50"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invitationsType", propOrder = {
    "invitation"
})
@XmlRootElement(name="invitations")
public class InvitationsType {

    @XmlElement(required = true)
    protected List<InvitationType> invitation;

    /**
     * Gets the value of the invitation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invitation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvitation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvitationType }
     * 
     * 
     */
    public List<InvitationType> getInvitation() {
        if (invitation == null) {
            invitation = new ArrayList<InvitationType>();
        }
        return this.invitation;
    }

}
