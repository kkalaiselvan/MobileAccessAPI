
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hidglobal.managedservices.mobileaccess package. 
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

    private final static QName _CreateInvitationRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "CreateInvitationRequest");
    private final static QName _DeleteDeviceRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "DeleteDeviceRequest");
    private final static QName _RevokeCredentialRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "RevokeCredentialRequest");
    private final static QName _OrderProcessingRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "OrderProcessingRequest");
    private final static QName _UpdateCredentialRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "UpdateCredentialRequest");
    private final static QName _CreateInvitationResponse_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "CreateInvitationResponse");
    private final static QName _CancelInvitationRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "CancelInvitationRequest");
    private final static QName _IssueCredentialRequest_QNAME = new QName("http://managedservices.hidglobal.com/mobileaccess/", "IssueCredentialRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hidglobal.managedservices.mobileaccess
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InvitationsType }
     * 
     */
    public InvitationsType createInvitationsType() {
        return new InvitationsType();
    }

    /**
     * Create an instance of {@link CredentialsType }
     * 
     */
    public CredentialsType createCredentialsType() {
        return new CredentialsType();
    }

    /**
     * Create an instance of {@link CreateInvitationResponseType }
     * 
     */
    public CreateInvitationResponseType createCreateInvitationResponseType() {
        return new CreateInvitationResponseType();
    }

    /**
     * Create an instance of {@link IssueCredentialRequestType }
     * 
     */
    public IssueCredentialRequestType createIssueCredentialRequestType() {
        return new IssueCredentialRequestType();
    }

    /**
     * Create an instance of {@link DeviceType }
     * 
     */
    public DeviceType createDeviceType() {
        return new DeviceType();
    }

    /**
     * Create an instance of {@link RevokeCredentialRequestType }
     * 
     */
    public RevokeCredentialRequestType createRevokeCredentialRequestType() {
        return new RevokeCredentialRequestType();
    }

    /**
     * Create an instance of {@link UpdateCredentialRequestType }
     * 
     */
    public UpdateCredentialRequestType createUpdateCredentialRequestType() {
        return new UpdateCredentialRequestType();
    }

    /**
     * Create an instance of {@link CancelInvitationRequestType }
     * 
     */
    public CancelInvitationRequestType createCancelInvitationRequestType() {
        return new CancelInvitationRequestType();
    }

    /**
     * Create an instance of {@link CredentialType }
     * 
     */
    public CredentialType createCredentialType() {
        return new CredentialType();
    }

    /**
     * Create an instance of {@link DevicesType }
     * 
     */
    public DevicesType createDevicesType() {
        return new DevicesType();
    }

    /**
     * Create an instance of {@link InvitationType }
     * 
     */
    public InvitationType createInvitationType() {
        return new InvitationType();
    }

    /**
     * Create an instance of {@link CreateInvitationRequestType }
     * 
     */
    public CreateInvitationRequestType createCreateInvitationRequestType() {
        return new CreateInvitationRequestType();
    }

    /**
     * Create an instance of {@link OrderProcessingRequestType }
     * 
     */
    public OrderProcessingRequestType createOrderProcessingRequestType() {
        return new OrderProcessingRequestType();
    }

    /**
     * Create an instance of {@link DeleteDeviceRequestType }
     * 
     */
    public DeleteDeviceRequestType createDeleteDeviceRequestType() {
        return new DeleteDeviceRequestType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInvitationRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "CreateInvitationRequest")
    public JAXBElement<CreateInvitationRequestType> createCreateInvitationRequest(CreateInvitationRequestType value) {
        return new JAXBElement<CreateInvitationRequestType>(_CreateInvitationRequest_QNAME, CreateInvitationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteDeviceRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "DeleteDeviceRequest")
    public JAXBElement<DeleteDeviceRequestType> createDeleteDeviceRequest(DeleteDeviceRequestType value) {
        return new JAXBElement<DeleteDeviceRequestType>(_DeleteDeviceRequest_QNAME, DeleteDeviceRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RevokeCredentialRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "RevokeCredentialRequest")
    public JAXBElement<RevokeCredentialRequestType> createRevokeCredentialRequest(RevokeCredentialRequestType value) {
        return new JAXBElement<RevokeCredentialRequestType>(_RevokeCredentialRequest_QNAME, RevokeCredentialRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderProcessingRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "OrderProcessingRequest")
    public JAXBElement<OrderProcessingRequestType> createOrderProcessingRequest(OrderProcessingRequestType value) {
        return new JAXBElement<OrderProcessingRequestType>(_OrderProcessingRequest_QNAME, OrderProcessingRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateCredentialRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "UpdateCredentialRequest")
    public JAXBElement<UpdateCredentialRequestType> createUpdateCredentialRequest(UpdateCredentialRequestType value) {
        return new JAXBElement<UpdateCredentialRequestType>(_UpdateCredentialRequest_QNAME, UpdateCredentialRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInvitationResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "CreateInvitationResponse")
    public JAXBElement<CreateInvitationResponseType> createCreateInvitationResponse(CreateInvitationResponseType value) {
        return new JAXBElement<CreateInvitationResponseType>(_CreateInvitationResponse_QNAME, CreateInvitationResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelInvitationRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "CancelInvitationRequest")
    public JAXBElement<CancelInvitationRequestType> createCancelInvitationRequest(CancelInvitationRequestType value) {
        return new JAXBElement<CancelInvitationRequestType>(_CancelInvitationRequest_QNAME, CancelInvitationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueCredentialRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://managedservices.hidglobal.com/mobileaccess/", name = "IssueCredentialRequest")
    public JAXBElement<IssueCredentialRequestType> createIssueCredentialRequest(IssueCredentialRequestType value) {
        return new JAXBElement<IssueCredentialRequestType>(_IssueCredentialRequest_QNAME, IssueCredentialRequestType.class, null, value);
    }

}
