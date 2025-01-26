package com.appcircle.keycloak.listener.repository;

import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EventEntityRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<KeyCloakEventEntity> queryEventEntityByEventTime(Optional<Long> eventTime) {
        String whereCondition = eventTime.map(eventTimeValue -> " where event_time > ? ").orElse("");
        String orderBy = " order by event_time asc limit 10";
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
}
