package com.kamalteja.brevify;

import com.kamalteja.brevify.shortenerService.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableCaching
public class BrevifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrevifyApplication.class, args);
    }

}
