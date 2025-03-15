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
import com.kamalteja.brevify.shortenerService.exception.InternalServerErrorException;
import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import com.kamalteja.brevify.shortenerService.service.IShortenerService;
import com.kamalteja.brevify.shortenerService.util.ShortenerUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    @Override
    public Map<String, String> shortenUrl(ShortenRequestDTO shortenRequestDTO) {
        log.info("in ShortenerServiceImpl -> shortenUrl - {}", shortenRequestDTO.url());
        String shortCode = shortenerUtility.shortenUrl(shortenRequestDTO.url());
        if (!StringUtils.hasText(applicationProperties.baseUrl()) && !StringUtils.hasText(shortCode)) {
            log.error("baseUrl or shortCode is empty");
            throw new InternalServerErrorException();
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
                Map<String, String> response = new HashMap<>();
                response.put(SHORT_URL, shortUrl);
                return response;
            } catch (JsonProcessingException e) {
                throw new MessagePublishingException();
            }
        }
    }

    @Override
    public CodeUrlMappingDTO getShortCodeData(String shortCode) {
        CodeUrlMapping codeUrlMapping = codeUrlMappingDAO.findByShortCode(shortCode);
        if (codeUrlMapping == null) {
            log.error("Shortcode not found");
            throw new CodeNotFoundException();
        }
        return new CodeUrlMappingDTO(codeUrlMapping);
    }

    @Override
    public String fetchLongUrlByShortCode(String shortCode) {
        log.info("in ShortenerServiceImpl -> fetchLongUrlByShortCode - {}", shortCode);

        String longUrl = shortenerCacheService.getCachedLongUrl(shortCode);
        if (StringUtils.hasText(longUrl)) {
            return longUrl;
        } else {
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
            if (!StringUtils.hasText(codeUrlMapping.getLongUrl())) {
                shortenerCacheService.cacheShortUrl(shortCode, longUrl);
            }
            return codeUrlMapping.getLongUrl();
        }
    }
}
