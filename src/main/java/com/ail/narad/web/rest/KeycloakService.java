package com.ail.narad.web.rest;


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ail.narad.web.rest.ServicesConfiguration;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class KeycloakService {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(KeycloakService.class);

    @Inject
    ServicesConfiguration servicesConfiguration;

    public String getAccessTokenForFCM() throws URISyntaxException, JSONException {
    	
        String keyCloakEndpoint = servicesConfiguration.getKeyCloakURL();
        URI uri = new URI(keyCloakEndpoint);
        

        RestTemplate temp = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", "application/x-www-form-urlencoded");

        log.info("About to hit the Keycloak URL with username: "+servicesConfiguration.getKeyCloakUser());
        HttpEntity<String> entity = new HttpEntity<>("username="+servicesConfiguration.getKeyCloakUser()+"&password="+servicesConfiguration.getKeyCloakPassword()+"&client_id="+servicesConfiguration.getKeyCloakClientId(),httpHeaders);
        log.info("Hit the Keyclaok URL");
        
        ResponseEntity responseEntity = temp.postForEntity(uri, entity, String.class);
        log.info("Sanjiv"+responseEntity.getBody().toString());
        JSONObject jsonObject = new JSONObject(responseEntity.getBody().toString());

        String accessToken = jsonObject.getString("access_token");

        log.info("THE FCM TOKEN IS "+accessToken);

        return accessToken;
    }

}