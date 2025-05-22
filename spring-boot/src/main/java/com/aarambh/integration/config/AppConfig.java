package com.aarambh.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${aarambh.unique-key:}")
    private String uniqueKey;

    @Value("${aarambh.subscriber-id:}")
    private String subscriberId;

    @Value("${aarambh.private-key:}") // Private Key of NP used to transact on ONDC
    private String privateKey;

    @Value("${aarambh.aarambh-pk}") // Private key generated on Aarambh dashboard (Generate it through Settings -> Project Setting -> generate key)
    private String aarambhPrivateKey;


    @Value("${aarambh.integration-uri:https://integrations.aarambh.cloud/api/v1/integrations}")
    private String integrationUri;

    // Getters
    public String getUniqueKey() { return uniqueKey; }
    public String getSubscriberId() { return subscriberId; }
    public String getPrivateKey() { return privateKey; }
    public String getAarambhPrivateKey() { return aarambhPrivateKey; }
    public String getIntegrationUri() { return integrationUri; }
} 
