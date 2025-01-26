package com.appcircle.keycloak.listener.model;

import lombok.ToString;

@ToString
public class KeyCloakEventEntity {
    private String id;
    private String client_id;
    private String details_json;
    private String error;
    private String ip_address;
    private String realm_id;
    private String session_id;
    private Long event_time;
    private String type;
    private String user_id;
    private String details_json_long_value;

    public KeyCloakEventEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getDetails_json() {
        return details_json;
    }

    public void setDetails_json(String details_json) {
        this.details_json = details_json;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getRealm_id() {
        return realm_id;
    }

    public void setRealm_id(String realm_id) {
        this.realm_id = realm_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Long event_time) {
        this.event_time = event_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDetails_json_long_value() {
        return details_json_long_value;
    }

    public void setDetails_json_long_value(String details_json_long_value) {
        this.details_json_long_value = details_json_long_value;
    }

}
