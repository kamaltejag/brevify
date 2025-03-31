package com.kamalteja.brevify.kafkaService.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalteja.brevify.kafkaService.exception.MessageConsumingException;
import com.kamalteja.brevify.shortenerService.dao.ICodeUrlMappingDAO;
import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortenerConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ICodeUrlMappingDAO codeUrlMappingDAO;

    /**
     * Kafka listener method that consumes messages from the "short-url-topic" topic.
     * Deserializes the message into a CodeUrlMapping object and saves it to the database.
     *
     * @param message The JSON string message received from Kafka
     * @throws MessageConsumingException If there is an error processing the JSON message
     */
    @KafkaListener(topics = "short-url-topic", groupId = "brevify-group")
    public void saveCodeUrlMapping(String message) {
        try {
            CodeUrlMapping codeUrlMapping = objectMapper.readValue(message, CodeUrlMapping.class);
            log.info("Consumed message for: {}", codeUrlMapping.getLongUrl());
            codeUrlMappingDAO.save(codeUrlMapping);
        } catch (JsonProcessingException e) {
            throw new MessageConsumingException(List.of("short-url-topic"));
        }
    }
}
