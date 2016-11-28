package com.ail.narad.service.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtils {
	
	public static Logger logger = Logger.getLogger(HttpUtils.class);
	
	public static String doPostRequest(String urlToHit, Map<String, String> urlParams, Map<String, String> headers) {
		@SuppressWarnings("deprecation")
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlToHit);
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		if(urlParams != null) {
			Iterator<Map.Entry<String, String>> iterator = urlParams.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				params.add(new BasicNameValuePair(key, value));
			}
		}
		if(headers != null) {
			Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				httpPost.addHeader(key, value);
			}
		}
		try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		    logger.error(e);
		}
		/*
		 * Execute the HTTP Request
		 */
		try {
		    HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) {
		        // EntityUtils to get the response content
		        String content =  EntityUtils.toString(respEntity);
		        return content;
		    }
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		    logger.error(e);
		} catch (IOException e) {
		    e.printStackTrace();
		    logger.error(e);
		}
		return null;
	}
	
	
	public static String doGetRequest(String urlToHit, Map<String, String> urlParams, Map<String, String> headers) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
		
		String urlParamsStr = null;
		if(urlParams != null) {
			StringBuffer urlParamsStrBuf = new StringBuffer();
			Iterator<Map.Entry<String, String>> iterator = urlParams.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				if(urlParamsStrBuf.length() > 0 ) {
					urlParamsStrBuf.append("&");
				}
				urlParamsStrBuf.append(key).append("=").append(value);
				
			}
			urlParamsStr = urlParamsStrBuf.toString();
		}
		if(headers != null) {
			Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				httpGet.addHeader(key, value);
			}
		}
		
		try {
			if(StringUtils.trimToNull(urlParamsStr) != null ) {
				httpGet.setURI(new URI(urlToHit + "?" + urlParamsStr));
			} else {
				httpGet.setURI(new URI(urlToHit));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			logger.error(e);
		}
		
		/*
		 * Execute the HTTP Request
		 */
		try {
		    HttpResponse response = httpClient.execute(httpGet);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) {
		        // EntityUtils to get the response content
		        String content =  EntityUtils.toString(respEntity);
		        return content;
		    }
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		    logger.error(e);
		} catch (IOException e) {
		    e.printStackTrace();
		    logger.error(e);
		}
		return null;
	}
	
	public static String getRequestParameter(HttpServletRequest request, String key, String defaultValue) {
		String value = request.getParameter(key);
		if(value == null || value == "null") {
			return defaultValue;
		}
		return value;
	}
	
	public static String doPostRequest(String urlToHit, String body, Map<String, String> headers) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlToHit);

		if(headers != null) {
			Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				httpPost.addHeader(key, value);
			}
		}
		try {
		    httpPost.setEntity(new StringEntity(body));
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		    logger.error(e);
		}
		/*
		 * Execute the HTTP Request
		 */
		try {
		    HttpResponse response = httpClient.execute(httpPost);
		    HttpEntity respEntity = response.getEntity();

		    if (respEntity != null) {
		        // EntityUtils to get the response content
		        String content =  EntityUtils.toString(respEntity);
		        return content;
		    }
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		    logger.error(e);
		} catch (IOException e) {
		    e.printStackTrace();
		    logger.error(e);
		}
		return null;
	}
	
}

