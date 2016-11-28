package com.ail.narad.aspects;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class RequestPreprocessorAspect {
	
	private Logger logger = LogManager.getLogger(RequestPreprocessorAspect.class);


	@Pointcut("within(com.ail.narad.controllers..*) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	private void inControllerAndRequestMappingAnnotation() {};
	
	@Around("inControllerAndRequestMappingAnnotation()")
    public Object requestListener(ProceedingJoinPoint pjp) throws Throwable {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		//extract the request
		HttpServletRequest request = attr.getRequest();
		request.setAttribute("requestId", UUID.randomUUID());
		Enumeration<String> parameterKeys = request.getParameterNames();
		StringBuilder sb = new StringBuilder("");
		if(parameterKeys != null) {
			while(parameterKeys.hasMoreElements()) {
				if(sb.length() == 0) {
					sb.append("Parameters: ");
				} else {
					sb.append("&");
				}
				String key = parameterKeys.nextElement();
				sb.append(key).append(":").append(StringUtils.trimToEmpty(request.getParameter(key)));
			}
		}
		long startTime = System.currentTimeMillis();
		logger.info(pjp.getTarget().getClass().getName()+": Started Request- "+request.getAttribute("requestId")+", with Method - "+request.getMethod()+" , Protocol- "+request.getProtocol()+", URL - "+request.getRequestURL()+" "+sb.toString());
        Object retVal = pjp.proceed();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        logger.info("Destroyed Request- "+request.getAttribute("requestId")+", with Method - "+request.getMethod()+" , Protocol- "+request.getProtocol()+", URL - "+request.getRequestURL()+" "+sb.toString()+ " response time:"+elapsedTime+" ms");
        return retVal;
    }
	
}
