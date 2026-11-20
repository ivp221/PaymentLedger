package com.example.ledger.platform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {
    @Bean
    OpenAPI ledgerOpenApi() {
        return new OpenAPI().info(new Info()
                .title("PaymentLedger API")
                .version("v2")
                .description("Double-entry payment ledger. Errors are RFC 9457 problem+json."));
    }
}
