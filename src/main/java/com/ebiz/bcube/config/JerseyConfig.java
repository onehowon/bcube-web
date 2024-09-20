package com.ebiz.bcube.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("com.ebiz.bcube.domain.executives.api");
        // Add other configurations or filters if needed
    }

    @Bean
    public org.glassfish.jersey.apache.connector.ApacheConnectorProvider apacheConnectorProvider() {
        return new org.glassfish.jersey.apache.connector.ApacheConnectorProvider();
    }
}
