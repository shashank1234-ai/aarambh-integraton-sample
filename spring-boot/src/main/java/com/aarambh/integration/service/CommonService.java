package com.aarambh.integration.service;

import com.aarambh.integration.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.purejava.tweetnacl.Signature;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonService {
    private final AppConfig appConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommonService(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ResponseEntity<String> sendSettlementToAgency(Map<String, Object> payload, String requestType) {
        String bppUri = (String) ((Map<String, Object>) payload.get("context")).get("bpp_uri");
        String saEndPoint = bppUri + "/" + requestType;
        return makeRequestOverSa(payload, saEndPoint);
    }

    public ResponseEntity<String> makeRequestOverSa(Map<String, Object> payload, String endPoint) {
        String authHeader = createAuthorizationHeader(payload);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return postOnBgOrBap(endPoint, payload, headers);
    }

    public ResponseEntity<String> postOnBgOrBap(String url, Map<String, Object> payload, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public ResponseEntity<String> postOnBapBpp(Map<String, Object> requestPayload) {
        String authHeader = createAuthorizationHeader(requestPayload);
        String requestUri = extractRequestUri(requestPayload);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return postOnBgOrBap(requestUri, requestPayload, headers);
    }

    private String extractRequestUri(Map<String, Object> payload) {
        Map<String, Object> context = (Map<String, Object>) payload.get("context");
        String action = (String) context.get("action");
        String bppUri = (String) context.get("bpp_uri");
        
        // Extract the appropriate URI based on the action
        switch (action) {
            case "recon":
                return bppUri + "/on_recon";
            case "on_recon":
                return bppUri + "/recon";
            default:
                return bppUri + "/" + action;
        }
    }

    private String createSigningString(String digestBase64, Long created, Long expires) {
        if (created == null) {
            created = Instant.now().getEpochSecond();
        }
        if (expires == null) {
            expires = Instant.now().plusSeconds(3600).getEpochSecond();
        }
        return String.format("(created): %d\n(expires): %d\ndigest: BLAKE-512=%s", created, expires, digestBase64);
    }

    private String signResponse(String signingKey, String privateKey) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            Signature signature = new Signature(privateKeyBytes);
            byte[] signed = signature.sign(signingKey.getBytes());
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            throw new RuntimeException("Error signing response", e);
        }
    }

    private String createAuthorizationHeader(Map<String, Object> requestBody) {
        long created = Instant.now().getEpochSecond();
        long expires = Instant.now().plusSeconds(3600).getEpochSecond();
        
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            String digestBase64 = Base64.getEncoder().encodeToString(
                java.security.MessageDigest.getInstance("SHA-512").digest(requestBodyJson.getBytes())
            );
            
            String signingKey = createSigningString(digestBase64, created, expires);
            String signature = signResponse(signingKey, appConfig.getPrivateKey());

            return String.format(
                "Signature keyId=\"%s|%s|ed25519\",algorithm=\"ed25519\",created=\"%d\",expires=\"%d\",headers=\"(created) (expires) digest\",signature=\"%s\"",
                appConfig.getSubscriberId(),
                appConfig.getUniqueKey(),
                created,
                expires,
                signature
            );
        } catch (Exception e) {
            throw new RuntimeException("Error creating authorization header", e);
        }
    }

    public String createAuthorizationHeaderAarambh(Map<String, Object> requestBody) {
        long created = Instant.now().getEpochSecond();
        long expires = Instant.now().plusSeconds(3600).getEpochSecond();
        
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            String digestBase64 = Base64.getEncoder().encodeToString(
                java.security.MessageDigest.getInstance("SHA-512").digest(requestBodyJson.getBytes())
            );
            
            String signingKey = createSigningString(digestBase64, created, expires);
            return signResponse(signingKey, appConfig.getPrivateKey());
        } catch (Exception e) {
            throw new RuntimeException("Error creating Aarambh authorization header", e);
        }
    }
} 