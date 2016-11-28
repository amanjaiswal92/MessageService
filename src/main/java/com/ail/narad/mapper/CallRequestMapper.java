package com.ail.narad.mapper;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ail.narad.domain.CallRequest;


@Service
public class CallRequestMapper {

	public CallRequest CallRequestMap(JSONObject requestdata, String response) throws JSONException {
		System.out.println("in CallRequestMap ");
		CallRequest callRequest = new CallRequest();
		callRequest.setRequestId(requestdata.getString("requestid"));
		callRequest.setBody(requestdata.toString());
		callRequest.setPhoneno(requestdata.getString("phoneno"));
		callRequest.setResponse(response);
		callRequest.setConsignmentid(requestdata.getString("consignmentid"));
		callRequest.setCancelled(false);

		return callRequest;
	}

}
