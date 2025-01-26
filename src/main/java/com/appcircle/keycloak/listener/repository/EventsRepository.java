package com.appcircle.keycloak.listener.repository;

import com.appcircle.keycloak.listener.model.KeyCloakAdminEventEntity;
import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class EventsRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<KeyCloakEventEntity> queryEventEntityByEventTime(Optional<Long> eventTime) {
        String whereCondition = eventTime.map(eventTimeValue -> " where event_time > ? ").orElse("");
        String orderBy = " order by event_time asc ";
        String query = "select id, client_id, details_json, error, ip_address, realm_id, " +
                "session_id, event_time, type, user_id, details_json_long_value from event_entity " + whereCondition
                + orderBy;

        List<KeyCloakEventEntity> keyCloakEventEntities;

        if(eventTime.isPresent()) {
            keyCloakEventEntities =
                    eventTime.map(eventTimeValue -> jdbcTemplate.query(query,
                            new BeanPropertyRowMapper<>(KeyCloakEventEntity.class), eventTimeValue)).get();
        } else {
            keyCloakEventEntities = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(KeyCloakEventEntity.class));
        }

        return keyCloakEventEntities;
    }

    public List<KeyCloakAdminEventEntity> queryAdminEventEntityByEventTime(Optional<Long> eventTime) {
        String whereCondition = eventTime.map(eventTimeValue -> " where admin_event_time > ? ").orElse("");
        String orderBy = " order by admin_event_time asc ";
        String query = "select id, admin_event_time, realm_id, operation_type, auth_realm_id, auth_client_id, auth_user_id, ip_address, resource_path, representation, error, resource_type from admin_event_entity "
                + whereCondition
                + orderBy;

        List<KeyCloakAdminEventEntity> keyCloakEventEntities;

        if(eventTime.isPresent()) {
            keyCloakEventEntities =
                    eventTime.map(eventTimeValue -> jdbcTemplate.query(query,
                            new BeanPropertyRowMapper<>(KeyCloakAdminEventEntity.class), eventTimeValue)).get();
        } else {
            keyCloakEventEntities = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(KeyCloakAdminEventEntity.class));
        }

        return keyCloakEventEntities;
    }
}
