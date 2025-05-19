package com.aarambh.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${aarambh.unique-key:}")
    private String uniqueKey;

    @Value("${aarambh.subscriber-id:}")
    private String subscriberId;

    @Value("${aarambh.private-key:}")
    private String privateKey;

    @Value("${aarambh.aarambh-pk}")
    // @Value("${aarambh.access-key:}")
    // private String accessKey;

    // @Value("${aarambh.secret-key:}")
    // private String secretKey;

    @Value("${aarambh.integration-uri:https://integrations.aarambh.cloud/api/v1/integrations}")
    private String integrationUri;

    @Value("${aarambh.sa-endpoint:}")
    private String saEndPoint;

    // Getters
    public String getUniqueKey() { return uniqueKey; }
    public String getSubscriberId() { return subscriberId; }
    public String getPrivateKey() { return privateKey; }
    public String getAarambhPrivateKey() { return aarambhPrivateKey; }

    // public String getAccessKey() { return accessKey; }
    // public String getSecretKey() { return secretKey; }
    public String getIntegrationUri() { return integrationUri; }
    public String getSaEndPoint() { return saEndPoint; }
} 