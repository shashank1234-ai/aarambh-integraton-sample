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
    public ResponseEntity<String> settleTransaction(@Valid @RequestBody Object requestPayload) {
        return commonService.sendSettlementToAgency(requestPayload, "settle");
    }

    @PostMapping("/report")
    public ResponseEntity<String> settlementReport(@Valid @RequestBody Object requestPayload) {
        return commonService.sendSettlementToAgency(requestPayload, "report");
    }

    @PostMapping("/recon")
    public ResponseEntity<String> settlementRecon(
            @Valid @RequestBody Object requestPayload,
            @RequestHeader(value = "requesttype", required = false) String requestType) {
        if (requestType != null) {
            return commonService.postOnBapBpp(requestPayload);
        } else {
            IntegrationPayload payload = new IntegrationPayload("RECON", requestPayload);

            // Map<String, Object> payload = new HashMap<>();
            // payload.put("type", "RECON");
            // payload.put("data", requestPayload);
            return aarambhIntegrationService.updateRecord(payload);
        }
    }

    @PostMapping("/on_recon")
    public ResponseEntity<String> settlementOnRecon(
            @Valid @RequestBody Object requestPayload,
            @RequestHeader(value = "requesttype", required = false) String requestType) {
        if (requestType != null) {
            return commonService.postOnBapBpp(requestPayload);
        } else {
            // Map<String, Object> payload = new HashMap<>();
            // payload.put("type", "ON_RECON");
            // payload.put("data", requestPayload);
            IntegrationPayload payload = new IntegrationPayload("ON_RECON", requestPayload);

            return aarambhIntegrationService.updateRecord(payload);
        }
    }

    @PostMapping("/on_settle")
    public ResponseEntity<String> settlementOnSettle(@Valid @RequestBody Object requestPayload) {
        // Map<String, Object> payload = new HashMap<>();
        // payload.put("type", "ON_SETTLE");
        // payload.put("data", requestPayload);
        IntegrationPayload payload = new IntegrationPayload("ON_SETTLE", requestPayload);
        
        return aarambhIntegrationService.updateRecord(payload);
    }

    @PostMapping("/on_report")
    public ResponseEntity<String> settlementOnReport(@Valid @RequestBody Object requestPayload) {
        // Map<String, Object> payload = new HashMap<>();
        // payload.put("type", "ON_REPORT");
        // payload.put("data", requestPayload);
        IntegrationPayload payload = new IntegrationPayload("ON_REPORT", requestPayload);
        
        return aarambhIntegrationService.updateRecord(payload);
    }

    

        private static class IntegrationPayload {
        private String type;
        private Object data;

        public IntegrationPayload(String type, Object data) {
            this.type = type;
            this.data = data;
        }
        }
} 
