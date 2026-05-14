package com.ben.content_distribution_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Content Distribution API",
                version = "1.0.0",
                description = "REST API for managing film metadata with filtering, pagination, sorting, validation, PostgreSQL persistence, and Flyway migrations."
        )
)

public class OpenApiConfig {

}
