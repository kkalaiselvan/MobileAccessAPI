
package com.hidglobal.managedservices.mobileaccess;

import javax.xml.ws.WebFault;
import com.hidglobal.base.Faults;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "Faults", targetNamespace = "http://hidglobal.com/base/")
public class FaultMessage
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private Faults faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public FaultMessage(String message, Faults faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public FaultMessage(String message, Faults faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.hidglobal.base.Faults
     */
    public Faults getFaultInfo() {
        return faultInfo;
    }

}
