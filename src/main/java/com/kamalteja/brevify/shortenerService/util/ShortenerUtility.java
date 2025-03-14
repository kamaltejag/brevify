package com.kamalteja.brevify.shortenerService.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShortenerUtility {

    private final EncryptionUtil encryptionUtil;

    @Autowired
    public ShortenerUtility(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    public String shortenUrl(String url) {
        return encryptionUtil.hashUrl(url);
    }
}
