package com.sample.mskcluster.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "kafka.custom.config")
public class KafkaConfigProperties {

    private String source;
    private String response;
    private String bootstrapServer;
    private String applicationId;
    
}
