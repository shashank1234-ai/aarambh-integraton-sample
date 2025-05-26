package com.aarambh.integration.service;

import com.aarambh.integration.config.AppConfig;
import com.aarambh.integration.dto.PayloadDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

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

    public ResponseEntity<String> sendSettlementToAgency(Object payload, String requestType) {
        PayloadDTO payloadDTO = (PayloadDTO) payload;
        String bppUri = payloadDTO.getContext().getBpp_uri();
        String saEndPoint = bppUri + "/" + requestType;
        return makeRequestOverSa(payload, saEndPoint);
    }

    public ResponseEntity<String> makeRequestOverSa(Object payload, String endPoint) {
        String authHeader = createAuthorizationHeader(payload);
        HttpHeaders headers = new HttpHeaders();
        
        headers.set("Authorization", authHeader);
        
        return requestPost(endPoint, payload, headers);
    }

    public ResponseEntity<String> requestPost(String url, Object payload, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request   = new HttpEntity<>(payload, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public ResponseEntity<String> postOnBapBpp(Object requestPayload) {
        String authHeader = createAuthorizationHeader(requestPayload);
        String requestUri = extractRequestUri(requestPayload);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return requestPost(requestUri, requestPayload, headers);
    }

    private String extractRequestUri(Object payload) {
        PayloadDTO payloadDTO = (PayloadDTO) payload;
        String action = payloadDTO.getContext().getAction();
        String requestUri = payloadDTO.getContext().getBpp_uri();
        return requestUri + "/" + action;
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
            org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters privateKeyParams = new org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters(privateKeyBytes, 0);
            org.bouncycastle.crypto.signers.Ed25519Signer signer = new org.bouncycastle.crypto.signers.Ed25519Signer();
            signer.init(true, privateKeyParams);
            byte[] messageBytes = signingKey.getBytes(StandardCharsets.UTF_8);
            signer.update(messageBytes, 0, messageBytes.length);
            byte[] signed = signer.generateSignature();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            throw new RuntimeException("Error signing response", e);
        }
    }

    private String createAuthorizationHeader(Object requestBody) {
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

    public String createAuthorizationHeaderAarambh(Object requestBody) {
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
            throw new RuntimeException("Error creating Aarambh authorization header", e);
        }
    }
} 
