package com.ail.narad.service.util;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ail.narad.web.rest.KeycloakService;
import com.ail.narad.web.rest.ServicesConfiguration;
import com.ail.narad.domain.CallDetails;
import com.ail.narad.domain.CallRequest;
import com.ail.narad.mapper.CallDetailsMapper;
import com.ail.narad.mapper.CallRequestMapper;
import com.ail.narad.repository.CallDetailsRepository;
import com.ail.narad.repository.CallRequestRepository;
import com.ail.narad.service.IvrCallService;

@Component
public class IvrCallsUtils {

	@Inject
	private CallRequestMapper callRequestMapper;

	@Inject
	private CallDetailsMapper callDetailsMapper;

	@Inject
	private CallDetailsRepository callDetailsRepository;

	@Inject
	CallRequestRepository callRequestRepository;

	@Inject
	private IvrCallService ivrCallService;



	@Autowired(required = true)
	private ServicesConfiguration servicesConfiguration;

	@Autowired
	private KeycloakService keycloakService;
	
	@Value("${custom.storeCallUrl}")
	private String storeCallUrl;
	
	@Value("${custom.apiKey}")
	private String api_key;
	
	@Value("${custom.campaignName}")
	private String campaign_name;
	
	@Value("${custom.maxRetry}")
	private String maxRetry;
	


	public Boolean getCallPickStatus(JSONObject requestdata) {
		// TODO Auto-generated method stub
		Boolean pickStatus = false;
		try {
			if (requestdata.get("DialStatus").equals("answered"))
				pickStatus = true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pickStatus;
	}

	public void putCallDetailsInDB(JSONObject requestdata) throws JSONException {
		// TODO Auto-generated method stub
		CallDetails callDetails = callDetailsMapper.CallDetailsMap(requestdata);

		CallDetails result = callDetailsRepository.save(callDetails);
		callDetailsRepository.save(result);

	}

	public void putCallRequestInDB(JSONObject requestdata, String response) throws JSONException {
		// TODO Auto-generated method stub
		System.out.println("in putCallRequestInDB ");

		CallRequest callRequest = callRequestMapper.CallRequestMap(requestdata, response);
		System.out.println("    ----" + callRequest);
		CallRequest result = callRequestRepository.save(callRequest);
		callRequestRepository.save(result);
	}

	public String makeCall(JSONObject requestdata) throws JSONException {
		// TODO Auto-generated method stub

		String addCampaignUrl = storeCallUrl + "?" + "api_key=" + api_key + "&campaign_name=" + campaign_name
				+ "&PhoneNumber=" + requestdata.get("phoneno") + "&Name=" + getUuiStructure(requestdata)
				+ "&action=START&format=json";
		String response = ivrCallService.makeGetUrlCall(addCampaignUrl);
		// String response = null;
		System.out.println("-----addCampaignUrl" + addCampaignUrl);
		if (response != null && !response.trim().isEmpty())
			return response;
		else
			return "AddCampaign URL failed";

	}

	private String getUuiStructure(JSONObject requestdata) throws JSONException {

		return "requestid:" + requestdata.getString("requestid") + ",consignmentid:"
				+ requestdata.getString("consignmentid") + ",count:" + requestdata.getInt("count") + ",phoneno:"
				+ requestdata.getString("phoneno") + ",fcid:" + requestdata.getString("fcid");
	}

	public void getMessageAddTypeAndAddInQueue(JSONObject requestdata) throws JSONException, IOException, URISyntaxException {

		String uuiData = null;

		JSONObject response = new JSONObject();
		try {
			uuiData = requestdata.getString("UUI").trim();
			// phoneNo = requestdata.getString("DialedNumber").trim();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String requestId = ivrCallService.getKey(uuiData, "requestid");
		String consignmentid = ivrCallService.getKey(uuiData, "consignmentid");
		String count = ivrCallService.getKey(uuiData, "count");
		String phoneNo = ivrCallService.getKey(uuiData, "phoneno");
		String fcid = ivrCallService.getKey(uuiData, "fcid");

		int limit = Integer.parseInt(count);
		if (limit < Integer.parseInt(maxRetry) ) {
			response.put("consignmentid", consignmentid);
			response.put("count", Integer.parseInt(count) + 1);
			response.put("requestid", requestId);
			response.put("phoneno", phoneNo);
			response.put("fcid", fcid);

			ivrCallService.enqueInQueue(response, true);
			System.out.println("added in queue "+response);
		} else
			System.out
					.println("Count Exceeded for " + consignmentid + " phoneno " + phoneNo + " requestid " + requestId);

	}

/*	public boolean checkForConsignmentStatusAndFcid(String consignmentId, String fcId )
			throws JSONException, URISyntaxException, ClientProtocolException, IOException {
		// TODO Auto-generated method stub

	//	String consignmentId = requestdata.getString("consignmentid");
	//	String fcId = requestdata.getString("fcid");
	//	String assignedFcId = getActualFcId(consignmentId);
		String actualConsignmentStatus = null;
		String fcid = null;
		boolean addThisStore = false;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		String apiEndPoint = "/consignment/" + consignmentId;
		HttpGet httpGet = new HttpGet(servicesConfiguration.getOmsURL() + apiEndPoint);
System.out.println("==============="+servicesConfiguration.getOmsURL() + apiEndPoint);
		httpGet.setHeader("Authorization", "Bearer " + keycloakService.getAccessTokenForFCM());

		CloseableHttpResponse response = null;

		response = httpClient.execute(httpGet);
		org.apache.http.HttpEntity responseEntity = response.getEntity();

		// InputStream a = responseEntity.getContent();

		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		System.out.println(responseString);

		JSONObject consignmentDetails = new JSONObject(responseString);
		if (consignmentDetails.has("consignment")) {
			JSONObject consignment = consignmentDetails.getJSONObject("consignment");
			if (consignment.has("consignmentStatus")) {
				JSONObject consignmentStatus = consignment.getJSONObject("consignmentStatus");
				if (consignmentStatus.has("consignmentStatus"))
					actualConsignmentStatus = consignmentStatus.getString("consignmentStatus");
			}
			if (consignment.has("ffCenter")) {
				JSONObject ffCenter = consignment.getJSONObject("ffCenter");
				if (ffCenter.has("fcid"))
					fcid = ffCenter.getString("fcid");
			}

		}

		if(actualConsignmentStatus != null && fcid != null && actualConsignmentStatus.equals("ASSIGNED") && fcid.equals(fcId))
			addThisStore = true ;

		return addThisStore;
	}*/



}
