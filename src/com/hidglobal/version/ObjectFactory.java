
package com.hidglobal.version;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hidglobal.version package. 
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

    private final static QName _GetVersionRequest_QNAME = new QName("http://hidglobal.com/version/", "GetVersionRequest");
    private final static QName _GetVersionResponse_QNAME = new QName("http://hidglobal.com/version/", "GetVersionResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hidglobal.version
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetVersionResponseType }
     * 
     */
    public GetVersionResponseType createGetVersionResponseType() {
        return new GetVersionResponseType();
    }

    /**
     * Create an instance of {@link GetVersionRequestType }
     * 
     */
    public GetVersionRequestType createGetVersionRequestType() {
        return new GetVersionRequestType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/version/", name = "GetVersionRequest")
    public JAXBElement<GetVersionRequestType> createGetVersionRequest(GetVersionRequestType value) {
        return new JAXBElement<GetVersionRequestType>(_GetVersionRequest_QNAME, GetVersionRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hidglobal.com/version/", name = "GetVersionResponse")
    public JAXBElement<GetVersionResponseType> createGetVersionResponse(GetVersionResponseType value) {
        return new JAXBElement<GetVersionResponseType>(_GetVersionResponse_QNAME, GetVersionResponseType.class, null, value);
    }

}
