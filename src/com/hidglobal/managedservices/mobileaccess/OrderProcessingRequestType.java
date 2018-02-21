
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderProcessingRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderProcessingRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workorder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderProcessingRequestType", propOrder = {
    "workorder",
    "requestor"
})
@XmlRootElement(name="OrderProcessingRequest")
public class OrderProcessingRequestType {

    @XmlElement(required = true)
    protected String workorder;
    @XmlElement(required = true)
    protected String requestor;

    /**
     * Gets the value of the workorder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkorder() {
        return workorder;
    }

    /**
     * Sets the value of the workorder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkorder(String value) {
        this.workorder = value;
    }

    /**
     * Gets the value of the requestor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestor() {
        return requestor;
    }

    /**
     * Sets the value of the requestor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestor(String value) {
        this.requestor = value;
    }

}
