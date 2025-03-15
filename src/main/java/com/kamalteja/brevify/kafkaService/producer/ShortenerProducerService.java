package com.kamalteja.brevify.kafkaService.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortenerProducerService {
    private static final String TOPIC = "short-url-topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, String message) {
        log.info("Sending message to Kafka: {}", message);
        kafkaTemplate.send(TOPIC, key, message);
    }
}
