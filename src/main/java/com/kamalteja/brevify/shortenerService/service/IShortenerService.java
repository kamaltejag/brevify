package com.kamalteja.brevify.shortenerService.service;

import com.kamalteja.brevify.shortenerService.dto.CodeUrlMappingDTO;
import com.kamalteja.brevify.shortenerService.dto.ShortenRequestDTO;

import java.util.Map;

public interface IShortenerService {

    Map<String, String> shortenUrl(ShortenRequestDTO shortenRequestDTO);

    CodeUrlMappingDTO getShortCodeData(String shortCode);

    String fetchLongUrlByShortCode(String shortCode);
}
