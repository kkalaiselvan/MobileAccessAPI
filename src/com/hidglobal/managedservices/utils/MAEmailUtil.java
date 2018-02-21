package com.hidglobal.managedservices.utils;

import java.util.Map;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hidglobal.managedservices.exception.MobileAccessException;
import com.hidglobal.managedservices.vo.Email_V;
/**
 * 
 * This class trigger an email to the requested user regarding Invitation Information.
 *
 */
@Component
public class MAEmailUtil {
@Autowired
private AppCache appCache;
private static final Logger logger = LoggerFactory.getLogger(MAEmailUtil.class);
/**
 * This method is responsible for sending Invitation Email to the requested user.
 * @param invEmail
 *        This holds all information like "from","to","subject","body" and Host Address.
 * @throws EmailException
 * @throws MobileAccessException
 */
    public void sendHtmlEmail(Email_V invEmail) throws EmailException, MobileAccessException {
    	Map<String,String> hostDetails=appCache.getHostDetails();
    	HtmlEmail email = new HtmlEmail();
        email.setFrom(invEmail.getFrom());
        email.setSubject(invEmail.getSubject());
        email.addTo(invEmail.getTo());
        email.setMsg(invEmail.getBody());
        email.setSSLOnConnect(false);
        email.setHostName(hostDetails.get("HostName").toString());
        int port = Integer.parseInt(hostDetails.get("HostPort").toString());
        email.setSmtpPort(port);
        logger.debug("Email Triggered for the email Id [{}]",invEmail.getTo());
        email.send();
      
    }
  
}
