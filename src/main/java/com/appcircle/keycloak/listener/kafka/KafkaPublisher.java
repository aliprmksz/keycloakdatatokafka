package com.appcircle.keycloak.listener.kafka;

import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import com.appcircle.keycloak.listener.service.KeyCloakEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class KafkaPublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> publishEvent(KeyCloakEventDTO event) {
        return kafkaTemplate.send("keycloak-events", event.getId().toString(), event.getEventType() + event.getEvent_time());
    }
}
