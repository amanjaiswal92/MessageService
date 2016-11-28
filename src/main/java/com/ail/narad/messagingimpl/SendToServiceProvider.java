package com.ail.narad.messagingimpl;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.*;
import com.ail.narad.models.MessageSenderModel;
import com.ail.narad.service.AddToLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tech on 13/10/16.
 */

@Component
public class SendToServiceProvider {

    @Autowired
    MessageSenderModel messageSenderModel;

    @Autowired
    AddToLogService addToLogService;

    public void provider(String requestId,String type,String message){
        switch (type) {
            case "transactional_email":
                addToLogService.addToAuditTable(new Audit_logs(requestId, AuditEvent.SENT_TO_SERVICE_PROVIDER, "Transactional email service"));
                TransactionalEmailMessage messageBean = (TransactionalEmailMessage) TransactionalEmailMessage.deSerialize(message);
                messageSenderModel.sendMessage(requestId,TemplateType.TRANSACTIONAL_EMAIL, messageBean);
                break;
            case "transactional_sms":
                addToLogService.addToAuditTable(new Audit_logs(requestId, AuditEvent.SENT_TO_SERVICE_PROVIDER, "Transactional sms service"));
                TransactionalSMSMessage messageBean1 = (TransactionalSMSMessage) TransactionalSMSMessage.deSerialize(message);
                messageSenderModel.sendMessage(requestId,TemplateType.TRANSACTIONAL_SMS, messageBean1);
                break;
            case "promotional_email":
                addToLogService.addToAuditTable(new Audit_logs(requestId, AuditEvent.SENT_TO_SERVICE_PROVIDER, "Promotional email service"));
                PromotionalEmailMessage messageBean2 = (PromotionalEmailMessage) PromotionalEmailMessage.deSerialize(message);
                messageSenderModel.sendMessage(requestId,TemplateType.PROMOTIONAL_EMAIL, messageBean2);
                break;
            case "promotional_sms":
                addToLogService.addToAuditTable(new Audit_logs(requestId, AuditEvent.SENT_TO_SERVICE_PROVIDER, "Promotional sms service"));
                PromotionalSMSMessage messageBean3 = (PromotionalSMSMessage) PromotionalSMSMessage.deSerialize(message);
                messageSenderModel.sendMessage(requestId,TemplateType.PROMOTIONAL_SMS, messageBean3);
                break;
            case "dummy_transactional_email":
                addToLogService.addToAuditTable(new Audit_logs(requestId, AuditEvent.SENT_TO_SERVICE_PROVIDER, "Dummy Transactional email service"));
                DummyTransactionalEmailMessage messageBean4 = (DummyTransactionalEmailMessage) DummyTransactionalEmailMessage.deSerialize(message);
                messageSenderModel.sendMessage(requestId,TemplateType.DUMMY_TRANSACTIONAL_EMAIL, messageBean4);
                break;
            default:
                break;
            }
        }
}


