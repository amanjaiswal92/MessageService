package com.ail.narad.beans;

import com.ail.narad.service.util.LogUtils;

public class MsgResponseBean {

	private String message;
	
	private Boolean success;
	
	private String requestId;

	public MsgResponseBean(){}
	
	public MsgResponseBean(String message, Boolean success) {
		this.success = success;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getRequestId() {
		return LogUtils.getCurrentRequestId();
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
}
