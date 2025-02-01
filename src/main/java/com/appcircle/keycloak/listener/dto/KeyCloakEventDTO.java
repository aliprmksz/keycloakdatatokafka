package com.appcircle.keycloak.listener.dto;

import java.util.UUID;

public class KeyCloakEventDTO {
    private Long id;
    private Long event_time;
    private String eventType;

    private boolean isAdminEvent;

    public KeyCloakEventDTO() {
    }

    public boolean isAdminEvent() {
        return isAdminEvent;
    }

    public void setAdminEvent(boolean adminEvent) {
        isAdminEvent = adminEvent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Long event_time) {
        this.event_time = event_time;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
