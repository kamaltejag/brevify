package com.kamalteja.brevify.shortenerService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalteja.brevify.cacheService.ShortenerCacheService;
import com.kamalteja.brevify.kafkaService.exception.MessagePublishingException;
import com.kamalteja.brevify.kafkaService.producer.ShortenerProducerService;
import com.kamalteja.brevify.shortenerService.config.ApplicationProperties;
import com.kamalteja.brevify.shortenerService.dao.ICodeUrlMappingDAO;
import com.kamalteja.brevify.shortenerService.dto.CodeUrlMappingDTO;
import com.kamalteja.brevify.shortenerService.dto.ShortenRequestDTO;
import com.kamalteja.brevify.shortenerService.enums.CodeStatusEnum;
import com.kamalteja.brevify.shortenerService.exception.CodeExpiredException;
import com.kamalteja.brevify.shortenerService.exception.CodeInactiveException;
import com.kamalteja.brevify.shortenerService.exception.CodeNotFoundException;
import com.kamalteja.brevify.shortenerService.exception.ShortUrlCreationFailedException;
import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import com.kamalteja.brevify.shortenerService.service.IShortenerService;
import com.kamalteja.brevify.shortenerService.util.ShortenerUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static com.kamalteja.brevify.shortenerService.constants.ShortenerConstants.SHORT_URL;

@Service
@Slf4j
public class ShortenerServiceImpl implements IShortenerService {

    private final ShortenerUtility shortenerUtility;
    private final ApplicationProperties applicationProperties;
    private final ICodeUrlMappingDAO codeUrlMappingDAO;
    private final ShortenerProducerService shortenerProducerService;
    private final ShortenerCacheService shortenerCacheService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ShortenerServiceImpl(ShortenerUtility shortenerUtility,
                                ApplicationProperties applicationProperties, ICodeUrlMappingDAO codeUrlMappingDAO,
                                ShortenerProducerService shortenerProducerService, ShortenerCacheService shortenerCacheService) {
        this.shortenerUtility = shortenerUtility;
        this.applicationProperties = applicationProperties;
        this.codeUrlMappingDAO = codeUrlMappingDAO;
        this.shortenerProducerService = shortenerProducerService;
        this.shortenerCacheService = shortenerCacheService;
    }

    /**
     * Shortens a given URL by generating a short code and returns a map containing the short URL.
     * The method logs the process, validates the base URL and short code, and handles exceptions
     * related to message publishing. It also caches the short URL and sends a message to a Kafka topic.
     *
     * @param shortenRequestDTO the DTO containing the URL to be shortened
     * @return a map with the key "shortUrl" and the value as the generated short URL
     * @throws ShortUrlCreationFailedException if the base URL or short code is empty
     * @throws MessagePublishingException      if there is an error during message publishing
     */
    @Override
    public Map<String, String> shortenUrl(ShortenRequestDTO shortenRequestDTO) {
        log.info("in ShortenerServiceImpl -> shortenUrl - {}", shortenRequestDTO.url());
        String shortCode = shortenerUtility.shortenUrl(shortenRequestDTO.url());
        if (!StringUtils.hasText(applicationProperties.baseUrl()) && !StringUtils.hasText(shortCode)) {
            log.error("baseUrl or shortCode is empty");
            throw new ShortUrlCreationFailedException();
        } else {
            String shortUrl = applicationProperties.baseUrl() + shortCode;
            CodeUrlMapping codeUrlMapping = CodeUrlMapping.builder()
                    .longUrl(shortenRequestDTO.url())
                    .shortCode(shortCode)
                    .expiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(90)))
                    .status(CodeStatusEnum.ACTIVE.getStatus())
                    .build();
            try {
                String message = objectMapper.writeValueAsString(codeUrlMapping);
                shortenerProducerService.sendMessage(shortCode, message);
                shortenerCacheService.cacheShortUrl(shortCode, shortenRequestDTO.url());
                return Collections.singletonMap(SHORT_URL, shortUrl);
/**
 * Implementation of the IShortenerService interface, providing methods to shorten URLs,
 * retrieve short code data, and fetch long URLs by short code. This service utilizes
 * utility classes for URL shortening, caches short URLs, and communicates with a Kafka
 * producer for message publishing. It handles exceptions related to URL creation, message
 * publishing, and short code validation.
 */} catch (JsonProcessingException e) {
                throw new MessagePublishingException();
            }
        }
    }

    /**
     * Retrieves the data associated with a given short code.
     * If the short code is not found, a CodeNotFoundException is thrown.
     *
     * @param shortCode the short code to look up
     * @return a CodeUrlMappingDTO containing the data for the short code
     * @throws CodeNotFoundException if the short code does not exist
     */
    @Override
    public CodeUrlMappingDTO getShortCodeData(String shortCode) {
        CodeUrlMapping codeUrlMapping = codeUrlMappingDAO.findByShortCode(shortCode);
        if (codeUrlMapping == null) {
            log.error("Shortcode not found");
            throw new CodeNotFoundException();
        }
        return new CodeUrlMappingDTO(codeUrlMapping);
    }

    /**
     * Fetches the original long URL associated with a given short code.
     * It first attempts to retrieve the URL from the cache. If not found,
     * it queries the database. Throws exceptions if the short code is not found,
     * inactive, or expired.
     *
     * @param shortCode the short code to look up
     * @return the original long URL
     * @throws CodeNotFoundException if the short code does not exist or has no associated long URL
     * @throws CodeInactiveException if the short code is not active
     * @throws CodeExpiredException  if the short code has expired
     */
    @Override
    public String fetchLongUrlByShortCode(String shortCode) {
        log.info("in ShortenerServiceImpl -> fetchLongUrlByShortCode - {}", shortCode);

        String longUrl = shortenerCacheService.getCachedLongUrl(shortCode);
        if (StringUtils.hasText(longUrl)) {
            return longUrl;
        }
        CodeUrlMapping codeUrlMapping = codeUrlMappingDAO.findByShortCode(shortCode);
        if (codeUrlMapping == null || !StringUtils.hasText(codeUrlMapping.getLongUrl())) {
            log.error("Issue with the code mapping");
            throw new CodeNotFoundException();
        } else if (!codeUrlMapping.isCodeActive()) {
            log.error("Short url is not yet active");
            throw new CodeInactiveException();
        } else if (codeUrlMapping.isCodeExpired()) {
            log.error("Short url has expired");
            throw new CodeExpiredException();
        }
        longUrl = codeUrlMapping.getLongUrl();
        shortenerCacheService.cacheShortUrl(shortCode, longUrl);
        return longUrl;
    }
}
