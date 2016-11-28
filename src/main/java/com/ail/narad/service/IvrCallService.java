package com.ail.narad.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ail.narad.config.ApplicationProperties;
import com.ail.narad.config.util.AutowireHelper;
import com.ail.narad.domain.CallRequest;
import com.ail.narad.messagingimpl.RabbitMqClient;
import com.ail.narad.repository.custom.CustomCallRequestRepository;
import com.ail.narad.service.util.IvrCallsUtils;
import com.ail.narad.service.util.LogUtils;
import com.ail.narad.service.util.StringUtils;
import com.rabbitmq.client.AMQP;

@Component
public class IvrCallService {

	@Autowired
	private IvrCallsUtils ivrCallsUtils;

	@Inject
	private CustomCallRequestRepository customCallRequestRepository;

	private long minDelay = 180000;

	public void CallAndPutInDB(String data) throws URISyntaxException, ClientProtocolException, IOException {

		if (StringUtils.IsvalidJsonData(data)) {
			JSONObject requestdata = null;

			try {
				requestdata = new JSONObject(data);
				String response = null;
				String requestId = requestdata.getString("requestid");
				String consignmentId = requestdata.getString("consignmentid");
				String fcId = requestdata.getString("fcid");
				String phoneno = requestdata.getString("phoneno");

				CallRequest callRequest = customCallRequestRepository.findCancealStatusWithConsAndPhone(consignmentId, phoneno,requestId);

				// String assignedFcId = getActualFcId(consignmentId);
				// call CA AGENt addCampaign URL and gets response
				System.out.println("call request-----"+callRequest);
				if (callRequest != null) {
					if (callRequest.getCancelled() == false)
						response = ivrCallsUtils.makeCall(requestdata);
					else
						response = "Pick failed and assigned to diff store " + requestdata;
					
					System.out.println("saving response in DB ------------------");
					
					callRequest.setResponse(response);
					customCallRequestRepository.save(callRequest);

				} else {

					System.out.println("call request is null ");

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("someting went wrong in processing queue data ");
				e.printStackTrace();
			}
		} else
			System.out.println("Received '" + data + "'" + "   Data not valid");
	}

	public JSONObject addParametersInQueueData(String data) {
		JSONObject requestdata = null;
		if (StringUtils.IsvalidJsonData(data)) {
			String reqstid = LogUtils.getCurrentRequestId();

			reqstid = reqstid.substring(0, reqstid.lastIndexOf("-") - 1);

			try {
				requestdata = new JSONObject(data);
				if (!requestdata.has("requestid"))
					requestdata.put("requestid", reqstid);
				if (!requestdata.has("count"))
					requestdata.put("count", 1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("Your data is not in JSON form");
				e.printStackTrace();
			}
		} else {
			System.out.println("Your data is not in JSON form");
		}
		return requestdata;
	}

	public String makeGetUrlCall(String addCampaignURL) {

		StringBuilder result = new StringBuilder();
		URL url;
		BufferedReader rd = null;
		try {
			url = new URL(addCampaignURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			return result.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {
				rd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

	public String getKey(String uuiData, String key) {
		String subValue = uuiData.substring(uuiData.indexOf(key) + key.length() + 1, uuiData.length());

		return (subValue.indexOf(",") > 0) ? subValue.substring(0, subValue.indexOf(","))
				: subValue.substring(0, subValue.length());
	}

	public void enqueInQueue(JSONObject request, boolean delay) throws IOException {
		// TODO Auto-generated method stub

		// RabbitMqClient.getIVRChannel().queueDeclare("IVR_CALLS", true,
		// false, false, args);

		System.out.println(request);
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-delayed-type", "direct");
		RabbitMqClient.getIVRChannel().exchangeDeclare(RabbitMqClient.IvrExchangeName, "x-delayed-message", true, false,
				args);

		Map<String, Object> headers = new HashMap<String, Object>();
		if (delay)
			headers.put("x-delay", minDelay);
		// props.headers(headers);

		AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);

		// RabbitMqClient.getIVRChannel().exchangeDeclare("IVR_CALLS",
		// "x-delayed-message", true, false, headers);
		
	//	RabbitMqClient.bindIVRExgChannel("IVR_CALLS");

		try {
			RabbitMqClient.getIVRChannel().queueBind("IVR_CALLS", "IVR_calls", "IVR_CALLS");
			if (delay)
				RabbitMqClient.getIVRChannel().basicPublish(RabbitMqClient.IvrExchangeName, "IVR_CALLS", props.build(),
						request.toString().getBytes());
			else
				RabbitMqClient.getIVRChannel().basicPublish(RabbitMqClient.IvrExchangeName, "IVR_CALLS", props.build(),
						request.toString().getBytes());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" [x] Sent '" + request.toString() + "'");
		// System.out.println(LogUtils.getCurrentRequestId());

	}

}
