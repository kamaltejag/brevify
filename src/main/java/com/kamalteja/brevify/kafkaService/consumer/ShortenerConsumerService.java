package com.kamalteja.brevify.kafkaService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalteja.brevify.shortenerService.dao.ICodeUrlMappingDAO;
import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortenerConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ICodeUrlMappingDAO codeUrlMappingDAO;

    @KafkaListener(topics = "short-url-topic", groupId = "brevify-group")
    public void consume(String message) {
        try {
            CodeUrlMapping codeUrlMapping = objectMapper.readValue(message, CodeUrlMapping.class);
            log.info("Consumed message for: {}", codeUrlMapping.getLongUrl());
            codeUrlMappingDAO.save(codeUrlMapping);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
