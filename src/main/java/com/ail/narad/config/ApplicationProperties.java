package com.ail.narad.config;


import org.apache.commons.lang3.StringUtils;

import org.springframework.boot.bind.RelaxedPropertyResolver;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;

@Configuration
public class ApplicationProperties implements EnvironmentAware {

	private static RelaxedPropertyResolver customPropertyResolver;

	@Override
	public void setEnvironment(Environment environment) {
		customPropertyResolver = new RelaxedPropertyResolver(environment, "custom.");
	}

	public static String getCustomProperty(String key) {
		return customPropertyResolver.getProperty(key);

	}

	public static String getCustomProperty(String key, String defaultValue) {
		String value = customPropertyResolver.getProperty(key);
		if (StringUtils.trimToNull(value) == null) {
			return defaultValue;
		}
		return value;
	}



	/*
	 * @Override public void setApplicationContext(ApplicationContext
	 * applicationContext) throws BeansException { // TODO Auto-generated method
	 * stub ApplicationProperties.customPropertyResolver =
	 * (RelaxedPropertyResolver)
	 * applicationContext.getBean("RelaxedPropertyResolver.class");
	 * 
	 * }
	 */

}
