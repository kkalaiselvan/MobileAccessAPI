
package com.hidglobal.base;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hidglobal.base package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _HeaderTypeAttributes_QNAME = new QName("http://hidglobal.com/base/", "attributes");
    private final static QName _HeaderTypeEndTime_QNAME = new QName("http://hidglobal.com/base/", "endTime");
    private final static QName _HeaderTypeClientId_QNAME = new QName("http://hidglobal.com/base/", "clientId");
    private final static QName _HeaderTypeStartTime_QNAME = new QName("http://hidglobal.com/base/", "startTime");
    private final static QName _HeaderTypeTransactionId_QNAME = new QName("http://hidglobal.com/base/", "transactionId");
    private final static QName _Header_QNAME = new QName("http://hidglobal.com/base/", "Header");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hidglobal.base
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link FaultType }
     * 
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link Attributes }
     * 
     */
    public Attributes createAttributes() {
        return new Attributes();
    }

    /**
     * Create an instance of {@link Faults }
     * 
     */
    public Faults createFaults() {
        return new Faults();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Attributes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/base/", name = "attributes", scope = HeaderType.class)
    public JAXBElement<Attributes> createHeaderTypeAttributes(Attributes value) {
        return new JAXBElement<Attributes>(_HeaderTypeAttributes_QNAME, Attributes.class, HeaderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/base/", name = "endTime", scope = HeaderType.class)
    public JAXBElement<XMLGregorianCalendar> createHeaderTypeEndTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_HeaderTypeEndTime_QNAME, XMLGregorianCalendar.class, HeaderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/base/", name = "clientId", scope = HeaderType.class)
    public JAXBElement<String> createHeaderTypeClientId(String value) {
        return new JAXBElement<String>(_HeaderTypeClientId_QNAME, String.class, HeaderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/base/", name = "startTime", scope = HeaderType.class)
    public JAXBElement<XMLGregorianCalendar> createHeaderTypeStartTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_HeaderTypeStartTime_QNAME, XMLGregorianCalendar.class, HeaderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/base/", name = "transactionId", scope = HeaderType.class)
    public JAXBElement<String> createHeaderTypeTransactionId(String value) {
        return new JAXBElement<String>(_HeaderTypeTransactionId_QNAME, String.class, HeaderType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/base/", name = "Header")
    public JAXBElement<HeaderType> createHeader(HeaderType value) {
        return new JAXBElement<HeaderType>(_Header_QNAME, HeaderType.class, null, value);
    }

}
