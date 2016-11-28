package com.ail.narad.service;

import com.ail.narad.beans.MsgResponseBean;
import com.ail.narad.config.ApplicationProperties;
import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.domain.enumeration.TemplateStatus;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.models.AuditLogModel;
import com.ail.narad.models.MessagingModel;
import com.ail.narad.models.TemplateModel;
import com.ail.narad.service.util.LogUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.ail.narad.web.rest.KeycloakService.log;

@Service
public class QueueManagerServiceImpl implements QueueManagerService{
//	private final Logger log = LoggerFactory.getLogger(QueueManagerServiceImpl.class);

	@Autowired
	MessagingModel messagingModel;

	@Autowired
	AuditLogModel auditLogsModel;

	@Autowired
	AddToLogService addToLogService;

	@Autowired
	TemplateModel templateModel;

	@Override
	public MsgResponseBean sendForQueue(String type, String version, HttpServletRequest request) throws Exception {

        ObjectMapper objectMapper= new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request.getParameterMap());

        String templateId = request.getParameter("template_id");
		String data = request.getParameter("data");
		String metaInfo = request.getParameter("meta");

		if (type.equals("transactional_sms")) {
			metaInfo = metaInfo.replaceAll("\\s", "");
		}
		LogUtils.setAuditLogger(auditLogsModel);
		Audit_logs auditLog = new Audit_logs(AuditEvent.NEW, "");
		String requestId = auditLog.getRequestId().toString();
        addToLogService.addToRequestTable(requestId, type, requestBody);
		addToLogService.addToAuditTable(auditLog);

		Templates template = templateModel.getByTemplateId(templateId,TemplateType.valueOf(StringUtils.upperCase(type)));
		if (template == null) {
            log.error("Template is not valid.");
			return new MsgResponseBean("Not a valid template", false);
		}

		if (template.getStatus() != TemplateStatus.APPROVED) {
			return new MsgResponseBean("Template Not yet approved", false);
		}

		JSONObject dataJson;
		try {
			dataJson = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
            log.error("Data is not in valid format");
			return new MsgResponseBean("Data is not in a valid format", false);
		}
		JSONObject metaJson;
		try {
			metaJson = new JSONObject(metaInfo);
		} catch (JSONException e) {
			e.printStackTrace();
            log.error("Meta is not in valid format");
			return new MsgResponseBean("Meta is not in a valid format", false);
		}
		try {
			messagingModel.addToQueue(requestId,template, type, dataJson, metaJson, null);
		} catch (Exception e) {
            log.error("Message is not added in queue: " + e.getMessage());
			return new MsgResponseBean(e.getMessage(), false);
		}
		return new MsgResponseBean("Message successfully added to queue", true);

	}

	@Override
	public MsgResponseBean removeFromQueue() throws Exception {
		// TODO Auto-generated method stub

		return null;
	}

    @Override
    public MsgResponseBean sendForQueueMultiPart(String type, String version, HttpServletRequest request, MultipartFile[] files) throws Exception {
        ObjectMapper objectMapper= new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request.getParameterMap());
        String templateId = request.getParameter("template_id");
        String data = request.getParameter("data");
        String metaInfo = request.getParameter("meta");
        LogUtils.setAuditLogger(auditLogsModel);
        Audit_logs auditLog = new Audit_logs(AuditEvent.NEW, "");
        String requestId = auditLog.getRequestId().toString();
        addToLogService.addToRequestTable(requestId, type, requestBody);
        addToLogService.addToAuditTable(auditLog);

        Templates template = templateModel.getByTemplateId(templateId,
            TemplateType.valueOf(StringUtils.upperCase(type)));
        if (template == null) {
            log.error("Template is not valid.");
            return new MsgResponseBean("Not a valid template", false);
        }

        if (template.getStatus() != TemplateStatus.APPROVED) {
            log.error("Template is not approved.");
            return new MsgResponseBean("Template Not yet approved", false);
        }

        JSONObject dataJson;
        try {
            dataJson = new JSONObject(data);

        } catch (JSONException e) {
            e.printStackTrace();
            log.error("Data is not in valid format");
            return new MsgResponseBean("Data is not in a valid format", false);
        }
        JSONObject metaJson;
        try {
            metaJson = new JSONObject(metaInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("Meta is not in valid format");
            return new MsgResponseBean("Meta is not in a valid format", false);
        }

        if (type.equalsIgnoreCase("transactional_sms") || type.equalsIgnoreCase("promotional_sms")) {
            log.info("Cannot attach files to an SMS because message type is " + type);
            return new MsgResponseBean("Cannot attach files to an SMS", false);
        }

        String fileName = null;
        String msg = "";
        List<String> uploadedFiles = new ArrayList<String>();

        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                try {
                    fileName = files[i].getOriginalFilename();
                    byte[] bytes = files[i].getBytes();
                    File folder = new File(ApplicationProperties.getCustomProperty("local.attachmentstore.path"));
                    folder.mkdirs();
                    BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(
                        ApplicationProperties.getCustomProperty("local.attachmentstore.path") + "/" + fileName)));
                    buffStream.write(bytes);
                    buffStream.close();

                    uploadedFiles.add(fileName);
                    msg += "You have successfully uploaded " + fileName + "<br/>";
                } catch (Exception e) {
                    return new MsgResponseBean("You failed to upload " + fileName + ": " + e.getMessage(), false);
                }
            }

            try {
                messagingModel.addToQueue(requestId,template, type, dataJson, metaJson, uploadedFiles);
            } catch (Exception e) {
                log.error("Message is not added in queue "+ e.getMessage());
                return new MsgResponseBean(e.getMessage(), false);
            }
            log.info("Message successfully added to queue :" + msg);
            return new MsgResponseBean("Message successfully added to queue: " + msg, true);
        } else {
            log.info("Unable to upload. file is empty.");
            return new MsgResponseBean("Unable to upload. File is empty.", false);
        }

    }
}
