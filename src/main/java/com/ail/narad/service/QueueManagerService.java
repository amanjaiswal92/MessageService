package com.ail.narad.service;

import com.ail.narad.beans.MsgResponseBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface QueueManagerService{
	public MsgResponseBean sendForQueue(String type, String version, HttpServletRequest request) throws Exception;
	public MsgResponseBean removeFromQueue() throws Exception;
    public MsgResponseBean sendForQueueMultiPart(String type, String version, HttpServletRequest request, MultipartFile[] files) throws Exception;
}
