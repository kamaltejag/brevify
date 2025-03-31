package com.kamalteja.brevify.shortenerService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "url_clicks")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlClicks {

    @Id
    @GeneratedValue(generator = "UUID")
    private String id;

    private String shortCodeId;

    private Timestamp visitedAt;

    private String ipAddress;

    private String userAgent;

    private String referrer;
}
