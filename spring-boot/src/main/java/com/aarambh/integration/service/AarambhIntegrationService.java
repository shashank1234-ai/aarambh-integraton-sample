package com.aarambh.integration.service;

import com.aarambh.integration.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

@Service
public class AarambhIntegrationService {
    private final AppConfig appConfig;
    private final RestTemplate restTemplate;
    private final CommonService commonService;

    @Autowired
    public AarambhIntegrationService(AppConfig appConfig, CommonService commonService) {
        this.appConfig = appConfig;
        this.restTemplate = new RestTemplate();
        this.commonService = commonService;
    }

    public ResponseEntity<String> createRecord(Map<String, Object> payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("subscriberid", appConfig.getSubscriberId());
            headers.set("Authorization", commonService.createAuthorizationHeaderAarambh(payload));
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestUri = appConfig.getIntegrationUri() + "/create_record";
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            
            return restTemplate.exchange(requestUri, HttpMethod.POST, request, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getRawStatusCode())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating record: " + e.getMessage());
        }
    }

    public ResponseEntity<String> updateRecord(Map<String, Object> payload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("subscriberid", appConfig.getSubscriberId());
            headers.set("Authorization", commonService.createAuthorizationHeaderAarambh(payload));
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestUri = appConfig.getIntegrationUri() + "/update_record";
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            
            return restTemplate.exchange(requestUri, HttpMethod.POST, request, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getRawStatusCode())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating record: " + e.getMessage());
        }
    }
} 