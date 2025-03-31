package com.kamalteja.brevify.shortenerService.controller;

import com.kamalteja.brevify.baseService.handler.BaseResponseHandler;
import com.kamalteja.brevify.baseService.model.ApiResponse;
import com.kamalteja.brevify.shortenerService.dto.CodeUrlMappingDTO;
import com.kamalteja.brevify.shortenerService.dto.ShortenRequestDTO;
import com.kamalteja.brevify.shortenerService.service.IShortenerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static com.kamalteja.brevify.baseService.constants.StringConstants.SUCCESS;

@RestController
@Slf4j
@RequestMapping("/")
public class ShortenerController {

    private final IShortenerService shortenerService;

    public ShortenerController(IShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ApiResponse<Map<String, String>>> shortenUrl(@Valid @RequestBody ShortenRequestDTO shortenRequestDTO) {
        log.info("in ShortenerController -> shortenUrl");
        Map<String, String> response = shortenerService.shortenUrl(shortenRequestDTO);
        return BaseResponseHandler.handler(SUCCESS, response);
    }

    @GetMapping("/code/{shortCode}")
    public ResponseEntity<ApiResponse<CodeUrlMappingDTO>> getShortCodeData(@PathVariable String shortCode) {
        log.info("in ShortenerController -> getShortCodeData");
        CodeUrlMappingDTO codeUrlMappingDTO = shortenerService.getShortCodeData(shortCode);
        return BaseResponseHandler.handler(SUCCESS, codeUrlMappingDTO);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<String> redirectToLongUrl(@PathVariable String shortCode) {
        log.info("in ShortenerController -> redirectToLongUrl");
        String longUrl = shortenerService.fetchLongUrlByShortCode(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }
}
