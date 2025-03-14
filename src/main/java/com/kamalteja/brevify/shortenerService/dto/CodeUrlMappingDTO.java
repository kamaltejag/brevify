package com.kamalteja.brevify.shortenerService.dto;

import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeUrlMappingDTO {
    private String longUrl;
    private String shortCode;
    private Timestamp expiresAt;
    private String status;

    public CodeUrlMappingDTO(CodeUrlMapping codeUrlMapping) {
        this.longUrl = codeUrlMapping.getLongUrl();
        this.shortCode = codeUrlMapping.getShortCode();
        this.expiresAt = codeUrlMapping.getExpiresAt();
        this.status = codeUrlMapping.getStatus();
    }
}
