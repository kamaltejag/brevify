package com.kamalteja.brevify.shortenerService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamalteja.brevify.shortenerService.dto.CodeUrlMappingDTO;
import com.kamalteja.brevify.shortenerService.enums.CodeStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "code_url_mapping")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeUrlMapping {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String longUrl;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private Timestamp expiresAt;

    @Column(nullable = false)
    private String status;

    public CodeUrlMapping(CodeUrlMappingDTO codeUrlMappingDTO) {
        this.longUrl = codeUrlMappingDTO.getLongUrl();
        this.shortCode = codeUrlMappingDTO.getShortCode();
        this.expiresAt = codeUrlMappingDTO.getExpiresAt();
        this.status = codeUrlMappingDTO.getStatus();
    }

    @JsonIgnore
    public boolean isCodeActive() {
        return StringUtils.pathEquals(CodeStatusEnum.ACTIVE.getStatus(), this.status);
    }
    
    @JsonIgnore
    public boolean isCodeExpired() {
        return expiresAt.before(Timestamp.from(Instant.now()));
    }
}
