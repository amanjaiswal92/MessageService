package com.ail.narad.controllers;

import com.ail.narad.beans.MsgResponseBean;
import com.ail.narad.domain.CallRequest;
import com.ail.narad.models.AuditLogModel;
import com.ail.narad.models.MessagingModel;
import com.ail.narad.models.TemplateModel;
import com.ail.narad.repository.custom.CustomCallRequestRepository;
import com.ail.narad.service.AddToLogService;
import com.ail.narad.service.IvrCallService;
import com.ail.narad.service.QueueManagerService;
import com.ail.narad.service.util.IvrCallsUtils;
import com.ail.narad.service.util.LogUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessagingController {

	LogUtils logger = new LogUtils(MessagingController.class);
	private final static Logger log = LoggerFactory.getLogger(MessagingController.class);

	@Inject
	private MessagingModel messagingModel;

	@Inject
	private TemplateModel templateModel;

	@Inject
	private AuditLogModel auditLogsModel;

	@Autowired
	AddToLogService addToLogService;

	@Inject
	private IvrCallsUtils ivrCallsUtils;

	@Inject
	private IvrCallService ivrCallService;

	@Inject
	private CustomCallRequestRepository customCallRequestRepository;

	@Autowired
	QueueManagerService queueManagerService;

	@RequestMapping(value = "/{version}/{type}/sendMessage", method = RequestMethod.POST)
	public MsgResponseBean sendMessage(@PathVariable String type, @PathVariable String version,HttpServletRequest request) throws Exception{
		log.info("Request to add message in queue.");
		return queueManagerService.sendForQueue(type,version,request);
	}

    @RequestMapping(value = "/{version}/{type}/sendMultipartMessage", method = RequestMethod.POST)
    public MsgResponseBean sendMessageWithAttachments(@PathVariable String type, @PathVariable String version, HttpServletRequest request, @RequestParam("file") MultipartFile[] files) throws Exception {
        log.info("Request to add messages in queue.");
        return queueManagerService.sendForQueueMultiPart(type,version,request,files);
    }


//	@RequestMapping(value = "/{version}/{type}/sendMessage", method = RequestMethod.POST)
//	public MsgResponseBean sendMessage(@PathVariable String type, @PathVariable String version,
//			HttpServletRequest request) {
//
//		String templateId = request.getParameter("template_id");
//		String data = request.getParameter("data");
//		// Removing spaces to send messages for blank tagged emails or
//		// phonenumbers
//		String metaInfo = request.getParameter("meta");
//		String requestBody = request.getParameterMap().toString();
//		if (type.equals("transactional_sms")) {
//			metaInfo = metaInfo.replaceAll("\\s", "");
//		}
//		LogUtils.setAuditLogger(auditLogsModel);
//		Audit_logs auditLog = new Audit_logs(AuditEvent.NEW, "");
//		String requestId = auditLog.getRequestId().toString();
//		addToLogService.addToRequestTable(requestId, type, requestBody);
//		LogUtils.getAuditLogger().addToQueue(auditLog);
//
//		Templates template = templateModel.getByTemplateId(templateId,TemplateType.valueOf(StringUtils.upperCase(type)));
//		if (template == null) {
//			return new MsgResponseBean("Not a valid template", false);
//		}
//
//		if (template.getStatus() != TemplateStatus.APPROVED) {
//			return new MsgResponseBean("Template Not yet approved", false);
//		}
//
//		JSONObject dataJson;
//		try {
//			dataJson = new JSONObject(data);
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return new MsgResponseBean("Data is not in a valid format", false);
//		}
//		JSONObject metaJson;
//		try {
//			metaJson = new JSONObject(metaInfo);
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return new MsgResponseBean("Meta is not in a valid format", false);
//		}
//		try {
//			messagingModel.addToQueue(requestId,template, type, dataJson, metaJson, null);
//		} catch (Exception e) {
//			return new MsgResponseBean(e.getMessage(), false);
//		}
//		return new MsgResponseBean("Message successfully added to queue", true);
//	}

	@RequestMapping(value = "{version}/storecall", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> makeCall(@RequestParam(required = false) String data,
			@PathVariable String version) throws IOException, JSONException {

		System.out.println(data);
		JSONObject response = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
		String reqstid = LogUtils.getCurrentRequestId();
		if (data != null && !data.trim().isEmpty() && data.indexOf("consignmentid") >= 0 && data.indexOf("phoneno") >= 0
				&& data.indexOf("fcid") >= 0) {

			System.out.println(data);

			JSONObject request = ivrCallService.addParametersInQueueData(data);

			ivrCallsUtils.putCallRequestInDB(request, "");

			ivrCallService.enqueInQueue(request, false);

			reqstid = reqstid.substring(0, reqstid.lastIndexOf("-") - 1);

			headers.add("X-Total-Count", String.valueOf(1));
			headers.add("Current-Page", String.valueOf(1));
			headers.add("Page-Size", String.valueOf(1));
			return new ResponseEntity<>(response.put("requestid", reqstid).put("status", "Success"), headers,
					HttpStatus.OK);

		} else {
			System.out.println("Invalid data " + data);

			headers.add("X-Total-Count", String.valueOf(1));
			headers.add("Current-Page", String.valueOf(1));
			headers.add("Page-Size", String.valueOf(1));
			return new ResponseEntity<>(response.put("requestid", "No Id Generated").put("status", "Failed"), headers,
					HttpStatus.OK);

		}

	}

	@RequestMapping(value = "{version}/ivrcancelcall/{conid}/{phoneno}", method = RequestMethod.GET)
	public ResponseEntity test(@PathVariable String conid, @PathVariable String phoneno)
			throws IOException, JSONException, URISyntaxException {

		// boolean toCall =
		// ivrCallsUtils.checkForConsignmentStatusAndFcid(conid,phoneno);
		boolean status = false;
		List<CallRequest> callRequest = customCallRequestRepository.findCancealStatus(conid, phoneno, status);
		HttpHeaders headers = new HttpHeaders();

		if (callRequest != null && callRequest.size() == 1) {
			CallRequest cRequest = callRequest.get(0);
			cRequest.setCancelled(true);
			customCallRequestRepository.save(cRequest);
			return new ResponseEntity<>(cRequest, headers, HttpStatus.OK);
		} else {

			return new ResponseEntity<>(
					new JSONObject().put("Message", "consignmentid and phonen no is not unique")
							.put("consignmentid", conid).put("phoneno", phoneno).put("data", callRequest.toString()),
					headers, HttpStatus.OK);
		}
		// System.out.println(toCall);

	}

	@RequestMapping(value = "{version}/sendcallbackdetails", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> callBackDetails(@RequestParam(required = false) String data,
			@PathVariable String version) throws IOException, JSONException, URISyntaxException {
		Boolean callPicked = false;
		HttpHeaders headers = new HttpHeaders();
		JSONObject requestdata = null;

		if (data.indexOf("requestid") >= 0 && data.indexOf("count") >= 0 && data.indexOf("consignmentid") >= 0) {

			if (data != null && !data.trim().isEmpty()) {
				System.out.println("valid");
				System.out.println(data);

				try {
					requestdata = new JSONObject(data);
					callPicked = ivrCallsUtils.getCallPickStatus(requestdata);
					if (callPicked) {
						ivrCallsUtils.putCallDetailsInDB(requestdata);
						// insert into dataBase with status YES.

					}

					else {
						// insert into dadabase with status NO .
						// add same message in message QUEUE

						ivrCallsUtils.putCallDetailsInDB(requestdata);
						ivrCallsUtils.getMessageAddTypeAndAddInQueue(requestdata);

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("data is not in JSON form");
					e.printStackTrace();
				}
			} else {
				requestdata = new JSONObject();
				requestdata.put("message", "data not valid");

			}

		} else {
			requestdata = new JSONObject();
			requestdata.put("message", "not related to store call");
		}
		headers.add("X-Total-Count", String.valueOf(1));
		headers.add("Current-Page", String.valueOf(1));
		headers.add("Page-Size", String.valueOf(1));

		return new ResponseEntity<>(requestdata, headers, HttpStatus.OK);

	}

//	@RequestMapping(value = "/{version}/{type}/sendMultipartMessage", method = RequestMethod.POST)
//	public MsgResponseBean sendMessageWithAttachments(@PathVariable String type, @PathVariable String version,
//			HttpServletRequest request, @RequestParam("file") MultipartFile[] files) {
//
//		String templateId = request.getParameter("template_id");
//		String data = request.getParameter("data");
//		String metaInfo = request.getParameter("meta");
//		String requestBody = request.getParameterMap().toString();
//		LogUtils.setAuditLogger(auditLogsModel);
//		Audit_logs auditLog = new Audit_logs(AuditEvent.NEW, "");
//		String requestId = auditLog.getRequestId().toString();
//		addToLogService.addToRequestTable(requestId, type, requestBody);
//		LogUtils.getAuditLogger().addToQueue(auditLog);
//
//		Templates template = templateModel.getByTemplateId(templateId,
//		TemplateType.valueOf(StringUtils.upperCase(type)));
//		if (template == null) {
//			return new MsgResponseBean("Not a valid template", false);
//		}
//
//		if (template.getStatus() != TemplateStatus.APPROVED) {
//			return new MsgResponseBean("Template Not yet approved", false);
//		}
//
//		JSONObject dataJson;
//		try {
//			dataJson = new JSONObject(data);
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return new MsgResponseBean("Data is not in a valid format", false);
//		}
//		JSONObject metaJson;
//		try {
//			metaJson = new JSONObject(metaInfo);
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return new MsgResponseBean("Meta is not in a valid format", false);
//		}
//
//		if (type.equalsIgnoreCase("transactional_sms") || type.equalsIgnoreCase("promotional_sms")) {
//			return new MsgResponseBean("Cannot attach files to an SMS", false);
//		}
//
//		String fileName = null;
//		String msg = "";
//		List<String> uploadedFiles = new ArrayList<String>();
//
//		if (files != null && files.length > 0) {
//			for (int i = 0; i < files.length; i++) {
//				try {
//					fileName = files[i].getOriginalFilename();
//					byte[] bytes = files[i].getBytes();
//					File folder = new File(ApplicationProperties.getCustomProperty("local.attachmentstore.path"));
//					folder.mkdirs();
//					BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(
//							ApplicationProperties.getCustomProperty("local.attachmentstore.path") + "/" + fileName)));
//					buffStream.write(bytes);
//					buffStream.close();
//
//					uploadedFiles.add(fileName);
//					msg += "You have successfully uploaded " + fileName + "<br/>";
//				} catch (Exception e) {
//					return new MsgResponseBean("You failed to upload " + fileName + ": " + e.getMessage(), false);
//				}
//			}
//
//			try {
//				messagingModel.addToQueue(requestId,template, type, dataJson, metaJson, uploadedFiles);
//			} catch (Exception e) {
//				return new MsgResponseBean(e.getMessage(), false);
//			}
//			return new MsgResponseBean("Message successfully added to queue: " + msg, true);
//		} else {
//			return new MsgResponseBean("Unable to upload. File is empty.", false);
//		}
//	}
}
