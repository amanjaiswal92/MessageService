package com.ail.narad.factory.messagesenders;

import com.ail.narad.config.ApplicationProperties;
import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.AuditLogger;
import com.ail.narad.factory.MessagingFactory;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.factory.messages.TransactionalEmailMessage;
import com.ail.narad.service.AddToLogService;
import com.ail.narad.service.util.LogUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.List;

@Component
public class MailgunAPISender implements IMessageSender {

	private static Client client;

    @Autowired
    AddToLogService addToLogService;

	public static Client getClient() {
		String apiKey = ApplicationProperties.getCustomProperty("transactional_email.mailgun.apikey");
		if(client == null) {
			client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("api", apiKey));
		}
		return client;
	}

	@Override
	public void sendMessage(TemplateType templateType, IMessageBean messageBean) {
		System.out.println("Reached Mailgun API sender: "+ messageBean.serialize());
		AuditLogger auditLogger = LogUtils.getAuditLogger();
		MessagingFactory messagingFactory = new MessagingFactory(StringUtils.lowerCase(templateType.toString()));
		Client client = getClient();
		String domain = ApplicationProperties.getCustomProperty("transactional_email.mailgun.domain");
		String urlprefix = ApplicationProperties.getCustomProperty("transactional_email.mailgun.urlprefix");
		TransactionalEmailMessage message = (TransactionalEmailMessage)messageBean;

		if(message.getHasAttachment() && message.getAttachmentFileNames() != null && !message.getAttachmentFileNames().isEmpty()) {
			sendMultipartMessage(templateType, messageBean);
			return;
		}

		try {
		    WebResource webResource = client.resource(urlprefix+ domain + "/messages");
		    MultivaluedMapImpl formData = new MultivaluedMapImpl();
		    formData.add("from", message.getFromAddress());
		    List<String> toAddresses = message.getToAddresses();
		    for(int i = 0; i < toAddresses.size(); i++) {
		    	formData.add("to", toAddresses.get(i));
		    }
		    List<String> ccAddresses = message.getCcAddresses();
		    for(int i = 0; ccAddresses != null && i < ccAddresses.size(); i++) {
		    	formData.add("cc", ccAddresses.get(i));
		    }
		    List<String> bccAddresses = message.getBccAddresses();
		    for(int i = 0; bccAddresses != null && i < bccAddresses.size(); i++) {
		    	formData.add("bcc", bccAddresses.get(i));
		    }
		    formData.add("subject", message.getSubject());
		    formData.add("text", message.getMessage());
		    formData.add("html", message.getMessage());

		    if(StringUtils.trimToNull(message.getDeliveryDate()) != null) {
		    	formData.add("o:deliverytime", message.getDeliveryDate());
		    }

		    ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
		    String response = clientResponse.getEntity(String.class);
            addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.SENT_TO_SERVICE_PROVIDER, response));
		} catch(Exception e) {
			e.printStackTrace();
            addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.FAILED, "Failed at service provider"+ e.getMessage()));
		}
	}

	public void sendMultipartMessage(TemplateType templateType, IMessageBean messageBean) {

		AuditLogger auditLogger = LogUtils.getAuditLogger();
		MessagingFactory messagingFactory = new MessagingFactory(StringUtils.lowerCase(templateType.toString()));
		Client client = getClient();
		String domain = ApplicationProperties.getCustomProperty("transactional_email.mailgun.domain");
		String urlprefix = ApplicationProperties.getCustomProperty("transactional_email.mailgun.urlprefix");
		TransactionalEmailMessage message = (TransactionalEmailMessage)messageBean;
		try {
			WebResource webResource = client.resource(urlprefix+ domain + "/messages");
			FormDataMultiPart form = new FormDataMultiPart();
			form.field("from", message.getFromAddress());
			List<String> toAddresses = message.getToAddresses();
		    for(int i = 0; i < toAddresses.size(); i++) {
		    	form.field("to", toAddresses.get(i));
		    }
		    List<String> ccAddresses = message.getCcAddresses();
		    for(int i = 0; ccAddresses != null && i < ccAddresses.size(); i++) {
		    	form.field("cc", ccAddresses.get(i));
		    }
		    List<String> bccAddresses = message.getBccAddresses();
		    for(int i = 0; bccAddresses != null && i < bccAddresses.size(); i++) {
		    	form.field("bcc", bccAddresses.get(i));
		    }
		    form.field("subject", message.getSubject());
		    form.field("text", message.getMessage());
		    form.field("html", message.getMessage());

		    if(StringUtils.trimToNull(message.getDeliveryDate()) != null) {
		    	form.field("o:deliverytime", message.getDeliveryDate());
		    }

		    for(int i = 0; i < message.getAttachmentFileNames().size(); i++) {
		    	String fileName = message.getAttachmentFileNames().get(i);
		    	File file = new File(ApplicationProperties.getCustomProperty("local.attachmentstore.path")+"/"+fileName);
		    	if(fileName.endsWith(".txt")) {
					form.bodyPart(new FileDataBodyPart("attachment", file, MediaType.TEXT_PLAIN_TYPE));
		    	} else {
		    		form.bodyPart(new FileDataBodyPart("attachment", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		    	}
		    }
			ClientResponse clientResponse = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);
		    String response = clientResponse.getEntity(String.class);
            addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.SENT_TO_SERVICE_PROVIDER, response));
		} catch(Exception e) {
			e.printStackTrace();
            addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.FAILED, "Failed at service provider"+ e.getMessage()));
		}
	}

	@Override
	public void sendMessages(TemplateType templateType, List<IMessageBean> messageBeans) {
		for(int i = 0; i < messageBeans.size(); i++) {
			sendMessage(templateType, messageBeans.get(i));
		}
	}
}
