package com.kamalteja.brevify.kafkaService.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortenerProducerService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "short-url-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(CodeUrlMapping codeUrlMapping) {
        log.info("Sending message to Kafka: {}", codeUrlMapping.getLongUrl());
        try {
            String message = objectMapper.writeValueAsString(codeUrlMapping);
            kafkaTemplate.send(TOPIC, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
