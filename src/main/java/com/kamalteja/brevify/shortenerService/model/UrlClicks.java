package com.kamalteja.brevify.shortenerService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "url_clicks")
public class UrlClicks {

    @Id
    private String id;

    private String shortCodeId;

    private Timestamp visitedAt;

    private String ipAddress;

    private String userAgent;

    private String referrer;
}
