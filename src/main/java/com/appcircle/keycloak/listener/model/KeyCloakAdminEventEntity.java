package com.appcircle.keycloak.listener.model;

import lombok.ToString;

@ToString
public class KeyCloakAdminEventEntity {
    private String id;
    private Long admin_event_time;
    private String realm_id;
    private String operation_type;
    private String auth_realm_id;
    private String auth_client_id;
    private String auth_user_id;
    private String ip_address;
    private String resource_path;
    private String representation;
    private String error;
    private String resource_type;

    public KeyCloakAdminEventEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAdmin_event_time() {
        return admin_event_time;
    }

    public void setAdmin_event_time(Long admin_event_time) {
        this.admin_event_time = admin_event_time;
    }

    public String getRealm_id() {
        return realm_id;
    }

    public void setRealm_id(String realm_id) {
        this.realm_id = realm_id;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public String getAuth_realm_id() {
        return auth_realm_id;
    }

    public void setAuth_realm_id(String auth_realm_id) {
        this.auth_realm_id = auth_realm_id;
    }

    public String getAuth_client_id() {
        return auth_client_id;
    }

    public void setAuth_client_id(String auth_client_id) {
        this.auth_client_id = auth_client_id;
    }

    public String getAuth_user_id() {
        return auth_user_id;
    }

    public void setAuth_user_id(String auth_user_id) {
        this.auth_user_id = auth_user_id;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getResource_path() {
        return resource_path;
    }

    public void setResource_path(String resource_path) {
        this.resource_path = resource_path;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

}
