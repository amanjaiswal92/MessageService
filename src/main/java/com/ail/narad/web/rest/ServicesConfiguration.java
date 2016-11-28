package com.ail.narad.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServicesConfiguration implements EnvironmentAware {
	private final static Logger log = LoggerFactory.getLogger(ServicesConfiguration.class);

	private static RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment environment) {
		propertyResolver = new RelaxedPropertyResolver(environment, "services.");
	}

	public String getKeyCloakURL() {
		log.info("Keycloak URL IS: " + propertyResolver.getProperty("keycloak.url"), String.class, "");
		return propertyResolver.getProperty("keycloak.url", String.class, "");
	}

	public String getKeyCloakUser() {
		return propertyResolver.getProperty("keycloak.username", String.class, "");
	}

	public String getKeyCloakPassword() {
		return propertyResolver.getProperty("keycloak.password", String.class, "");
	}

	public String getKeyCloakClientId() {
		return propertyResolver.getProperty("keycloak.clientId", String.class, "");
	}

	public String getOmsURL() {
		return propertyResolver.getProperty("oms", String.class, "");
	}
	
	

}
