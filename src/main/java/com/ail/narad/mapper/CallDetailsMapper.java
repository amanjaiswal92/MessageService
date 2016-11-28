package com.ail.narad.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ail.narad.domain.CallDetails;
import com.ail.narad.service.IvrCallService;

@Service
public class CallDetailsMapper {
	
	@Inject
	private IvrCallService ivrCallService;

	public CallDetails CallDetailsMap(JSONObject requestdata) throws JSONException {
		CallDetails callDetails = new CallDetails();
		String uuiData= requestdata.getString("UUI").trim();
		String requestId= ivrCallService.getKey(uuiData,"requestid");
		String consignmentid= ivrCallService.getKey(uuiData,"consignmentid");
		String count= ivrCallService.getKey(uuiData,"count");
		String phoneNo =  ivrCallService.getKey(uuiData, "phoneno");
		
		String End = requestdata.get("EndTime").toString().trim();
		String Start = requestdata.get("StartTime").toString().trim();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime dateTimeStart = LocalDateTime.parse(Start, formatter);
		LocalDateTime dateTimeEnd = LocalDateTime.parse(End, formatter);
	
		callDetails.setAgentUniqueID(requestdata.getString("AgentUniqueID"));
		callDetails.setCallerId(requestdata.getString("CallerID"));
		callDetails.setDialedNumber(phoneNo);
		callDetails.setDuration(requestdata.getString("Duration"));
		callDetails.setEndTime(ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault()));
		callDetails.setLocation(requestdata.getString("Location"));
		callDetails.setRequestId(requestdata.getString("monitorUCID"));
		callDetails.setStartTime(ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault()));
		callDetails.setStatus(requestdata.getString("DialStatus"));
		callDetails.setTimeToAnswer(requestdata.getString("TimeToAnswer"));
		callDetails.setResponse(requestdata.toString());
		callDetails.setRequestId(requestId);
		callDetails.setOrderid(consignmentid);
		callDetails.setCount(Long.parseLong(count));
		
		return callDetails;
	}

}
