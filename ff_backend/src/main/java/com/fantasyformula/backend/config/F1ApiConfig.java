
package com.fantasyformula.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class F1ApiConfig {

    @Bean
    public RestClient f1RestClient(
            @Value("${fantasyf1.api.base-url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
