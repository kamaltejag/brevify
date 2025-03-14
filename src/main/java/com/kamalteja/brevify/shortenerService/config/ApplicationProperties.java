package com.kamalteja.brevify.shortenerService.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.brevify")
public record ApplicationProperties(String baseUrl) {
}
