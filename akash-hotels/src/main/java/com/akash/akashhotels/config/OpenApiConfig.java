package com.akash.akashhotels.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI akashHotelsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Akash Hotels API")
                        .description(
                                "REST API for Akash Hotels – a full-stack food ordering application. " +
                                "Use POST /api/auth/login to obtain a JWT token, then click 'Authorize' " +
                                "and paste the token to test secured endpoints."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Akash Hotels Dev Team")
                                .email("admin@akashhotels.com")))
                // Global security requirement – all endpoints require Bearer by default
                // (public endpoints are opened by SecurityConfig)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Paste the JWT token obtained from POST /api/auth/login")));
    }
}
