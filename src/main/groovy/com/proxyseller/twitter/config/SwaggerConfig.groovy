package com.proxyseller.twitter.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .group("Api")
                .pathsToMatch("/api/**")
                .build()
    }

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Application API")
                .version("1.0")
                .description("Twitter REST API. Spring Boot, MongoDB, Groovy, Gradle, Spock, Docker")
                .license(new License().name("Apache 2.0")
                        .url("http://springdoc.org"))
                .contact(new Contact().name("Kalchenko Serhii")
                        .email("test@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080")
                        .description("Dev service")))
    }
}