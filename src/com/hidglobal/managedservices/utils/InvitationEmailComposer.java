package com.hidglobal.managedservices.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.dao.InvitationDAO;
import com.hidglobal.managedservices.exception.InvitationException;
import com.hidglobal.managedservices.vo.Email_V;

@Component
public class InvitationEmailComposer {

	@Autowired
	private InvitationDAO invitationDAO;
	@Autowired
	private MAEmailUtil maEmailUtil;
	private static final Logger logger = LoggerFactory.getLogger(InvitationEmailComposer.class);
	public void sendInvitation(String companyId, Long invitationId)
			throws Exception {
		Map<String, Object> invitationDetails = new HashMap<String, Object>();
		invitationDetails = invitationDAO.fetchSendInvitationDetails(companyId,
				invitationId);
		if(invitationDetails.get("po_body")==null||invitationDetails.get("po_body").toString().isEmpty()){
			throw new InvitationException(companyId, null,
					"invalid.template", HIDContants.INVALIDVALUE,
					HIDContants.PRECONDITIONFAILED);
		}
		Email_V email = new Email_V();
		logger.info("Send Invitation Email triggered for Invitation Id [{}]",invitationId);
		email.setFrom(invitationDetails.get("po_from").toString());
		String[] toAddress = new String[] { invitationDetails.get("po_to")
				.toString() };
		email.setTo(toAddress);
		email.setSubject(invitationDetails.get("po_subject").toString());
		String text = MessageFormat.format(invitationDetails.get("po_body")
				.toString(), invitationDetails.get("po_first_name").toString(),
				invitationDetails.get("po_last_name").toString(),
				invitationDetails.get("po_aamk_invitation_code").toString(),
				invitationDetails.get("po_expiration_ts").toString());
		email.setBody(text);
		maEmailUtil.sendHtmlEmail(email);
	}
}
