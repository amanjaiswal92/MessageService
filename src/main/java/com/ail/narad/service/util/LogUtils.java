package com.ail.narad.service.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ail.narad.factory.AuditLogger;
import com.ail.narad.models.AuditLogModel;

public class LogUtils {

	private Logger logger;
	
	private Class<?> callingClass;
	
	public static AuditLogger auditLogger;
	
	public LogUtils(Class<?> c) {
		if(c == null) {
			callingClass = LogUtils.class;
		} else {
			callingClass = c;
		}
		logger = LogManager.getLogger(callingClass);
	}
	
	public void info(String message) {
		logger.info(callingClass.getName()+": Request Id- "+getCurrentRequestId() + ", " + message);
	}
	
	public void debug(String message) {
		logger.debug(callingClass.getName()+": Request Id- "+getCurrentRequestId() + ", " + message);
	}
	
	public void error(String message) {
		logger.error(callingClass.getName()+": Request Id- "+getCurrentRequestId() + ", " + message);
	}
	
	public void warn(String message) {
		logger.warn(callingClass.getName()+": Request Id- "+getCurrentRequestId() + ", " + message);
	}
	
	public void fatal(String message) {
		logger.fatal(callingClass.getName()+": Request Id- "+getCurrentRequestId() + ", " + message);
	}
	
	public void error(Exception exception) {
		logger.error(exception);
	}
	
	public void error(Object object) {
		logger.error(object);
	}

	public Logger getLogger() {
		return logger;
	}
	
	public static String getCurrentRequestId() {
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		//extract the request
			HttpServletRequest request = attr.getRequest();
			Object requestId = request.getAttribute("requestId");
			if(requestId != null) {
				return requestId.toString();
			}
		} catch(Exception e) {
		}
		return "No Id";
	}
	
	public static AuditLogger getAuditLogger() {
		return auditLogger;
	}
	
	public static void setAuditLogger(AuditLogModel  auditLogModel) {
		if(auditLogger == null)
			auditLogger = new AuditLogger(auditLogModel);
	}
	
	public void setAttribute(String key,Object value){
		try{
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attr.getRequest();
			if(request.getAttribute(key) == null){
				request.setAttribute(key, value);
			}
		}
		catch(Exception e){
			return;
		}
	}
}
