package com.ail.narad.factory.messages;

import com.google.gson.Gson;

import java.util.List;

public class PromotionalSMSMessage implements IMessageBean {

	private String message;

	private List<String> toNumbers;

	private String requestId;

    private String type;

    public String getType(){
        return type;
    }

    public String setType(String type){
        return this.type = type;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getToNumbers() {
		return toNumbers;
	}

	public void setToNumbers(List<String> toNumbers) {
		this.toNumbers = toNumbers;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String serialize() {
		return new Gson().toJson(this);
	}

	public static IMessageBean deSerialize(String str) {
		return new Gson().fromJson(str, PromotionalSMSMessage.class);
	}
}
