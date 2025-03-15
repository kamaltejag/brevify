package com.kamalteja.brevify.cacheService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ShortenerCacheService {
    private final StringRedisTemplate redisTemplate;
    private static final long EXPIRATION_TIME = 24; // Cache expiry in hours

    public void cacheShortUrl(String shortCode, String longUrl) {
        redisTemplate.opsForValue().set(shortCode, longUrl, EXPIRATION_TIME, TimeUnit.HOURS);
    }

    @Cacheable(value = "shortUrls", key = "#shortCode")
    public String getCachedLongUrl(String shortCode) {
        return redisTemplate.opsForValue().get(shortCode);
    }

    @CacheEvict(value = "shortUrls", key = "#shortCode")
    public void removeShortUrl(String shortCode) {
        redisTemplate.delete(shortCode);
    }
}
