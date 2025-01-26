package com.appcircle.keycloak.listener.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_event_query")
public class AdminEventQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long latestQueryEventTimestamp;

    public AdminEventQuery() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLatestQueryEventTimestamp() {
        return latestQueryEventTimestamp;
    }

    public void setLatestQueryEventTimestamp(Long latestQueryEventTimestamp) {
        this.latestQueryEventTimestamp = latestQueryEventTimestamp;
    }
}
