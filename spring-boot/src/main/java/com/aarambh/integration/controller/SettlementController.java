package com.aarambh.integration.controller;

import com.aarambh.integration.service.AarambhIntegrationService;
import com.aarambh.integration.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "*")
@Validated
public class SettlementController {
    private final CommonService commonService;
    private final AarambhIntegrationService aarambhIntegrationService;

    @Autowired
    public SettlementController(CommonService commonService, AarambhIntegrationService aarambhIntegrationService) {
        this.commonService = commonService;
        this.aarambhIntegrationService = aarambhIntegrationService;
    }

    @PostMapping("/settle")
    public ResponseEntity<String> settleTransaction(@Valid @RequestBody Map<String, Object> requestPayload) {
        validateContext(requestPayload);
        return commonService.sendSettlementToAgency(requestPayload, "settle");
    }

    @PostMapping("/report")
    public ResponseEntity<String> settlementReport(@Valid @RequestBody Map<String, Object> requestPayload) {
        validateContext(requestPayload);
        return commonService.sendSettlementToAgency(requestPayload, "report");
    }

    @PostMapping("/recon")
    public ResponseEntity<String> settlementRecon(
            @Valid @RequestBody Map<String, Object> requestPayload,
            @RequestHeader(value = "requesttype", required = false) String requestType) {
        
        validateContext(requestPayload);
        if (requestType != null) {
            return commonService.postOnBapBpp(requestPayload);
        } else {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "RECON");
            payload.put("data", requestPayload);
            return aarambhIntegrationService.updateRecord(payload);
        }
    }

    @PostMapping("/on_recon")
    public ResponseEntity<String> settlementOnRecon(
            @Valid @RequestBody Map<String, Object> requestPayload,
            @RequestHeader(value = "requesttype", required = false) String requestType) {
        
        validateContext(requestPayload);
        if (requestType != null) {
            return commonService.postOnBapBpp(requestPayload);
        } else {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "ON_RECON");
            payload.put("data", requestPayload);
            return aarambhIntegrationService.updateRecord(payload);
        }
    }

    @PostMapping("/on_settle")
    public ResponseEntity<String> settlementOnSettle(@Valid @RequestBody Map<String, Object> requestPayload) {
        validateContext(requestPayload);
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ON_SETTLE");
        payload.put("data", requestPayload);
        return aarambhIntegrationService.updateRecord(payload);
    }

    @PostMapping("/on_report")
    public ResponseEntity<String> settlementOnReport(@Valid @RequestBody Map<String, Object> requestPayload) {
        validateContext(requestPayload);
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ON_REPORT");
        payload.put("data", requestPayload);
        return aarambhIntegrationService.updateRecord(payload);
    }

    private void validateContext(Map<String, Object> payload) {
        if (!payload.containsKey("context")) {
            throw new IllegalArgumentException("Request payload must contain 'context' field");
        }
        Map<String, Object> context = (Map<String, Object>) payload.get("context");
        if (!context.containsKey("bpp_uri")) {
            throw new IllegalArgumentException("Context must contain 'bpp_uri' field");
        }
    }
} 