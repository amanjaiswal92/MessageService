package com.ail.narad.config;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AngularJSForwardController {
	private final Logger log = LoggerFactory.getLogger(AngularJSForwardController.class);

	@RequestMapping(value = { "/login**", "/activate*", "/password*", "/register*", "/reset/finish*", "/reset/request*",
	        "/sessions*", "/settings*", "/social-register/*", "/audits*", "/configuration*", "/docs*", "/apphealth*",
	        "/logs*", "/appmetrics*", "/user-management*", "/user-management/*", "/error*",
	        "/accessdenied*" }, method = RequestMethod.GET)
	public void pageForward(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
	    forward(httpRequest, httpResponse);
	}

	private void forward(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
	    RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/index.html");
	    try {
	        dispatcher.forward(httpRequest, httpResponse);
	    } catch (Exception e) {
	        log.error("Error forwarding request", e);
	    }
	}
}
