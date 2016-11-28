package com.ail.narad.factory.messages;

import com.google.gson.Gson;

import java.util.List;

public class DummyTransactionalEmailMessage implements IMessageBean {


	private String subject;

	private String message;

	private String fromAddress;

	private List<String> toAddresses;

	private List<String> ccAddresses;

	private List<String> bccAddresses;

	private Boolean isHTMLMessage = true;

	private Boolean hasAttachment = false;

	private List<String> attachmentFileNames;

	private String deliveryDate;

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

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public List<String> getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	public List<String> getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(List<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public List<String> getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(List<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public Boolean getIsHTMLMessage() {
		return isHTMLMessage;
	}

	public void setIsHTMLMessage(Boolean isHTMLMessage) {
		this.isHTMLMessage = isHTMLMessage;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Boolean getHasAttachment() {
		return hasAttachment;
	}

	public void setHasAttachment(Boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public List<String> getAttachmentFIleNames() {
		return attachmentFileNames;
	}

	public void setAttachmentFIleNames(List<String> attachmentFileNames) {
		this.attachmentFileNames = attachmentFileNames;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@Override
	public String serialize() {
		return new Gson().toJson(this);
	}

	public static IMessageBean deSerialize(String str) {
		return new Gson().fromJson(str, DummyTransactionalEmailMessage.class);
	}

}
