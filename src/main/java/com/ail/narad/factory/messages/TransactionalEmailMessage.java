package com.ail.narad.factory.messages;

import com.google.gson.Gson;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TransactionalEmailMessage implements IMessageBean,Serializable {


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





	@Override
	public String serialize() {
		return new Gson().toJson(this);
	}

	public static IMessageBean deSerialize(String str) {
		return new Gson().fromJson(str, TransactionalEmailMessage.class);
	}

}
