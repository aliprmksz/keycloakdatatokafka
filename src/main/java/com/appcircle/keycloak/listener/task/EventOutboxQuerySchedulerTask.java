package com.appcircle.keycloak.listener.task;

import com.appcircle.keycloak.listener.kafka.KafkaPublisher;
import com.appcircle.keycloak.listener.model.EventOutbox;
import com.appcircle.keycloak.listener.model.EventQuery;
import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import com.appcircle.keycloak.listener.service.EventOutboxService;
import com.appcircle.keycloak.listener.service.EventQueryService;
import com.appcircle.keycloak.listener.service.KeyCloakEventDTO;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class EventOutboxQuerySchedulerTask {

    @Autowired
    private EventOutboxService eventOutBoxEventService;

    @Autowired
    private KafkaPublisher kafkaPublisher;


    @Scheduled(cron = "*/15 * * * * *")
    @SchedulerLock(name = "EventOutboxQuerySchedulerTask.scheduledTask", lockAtLeastFor = "PT10S", lockAtMostFor = "PT15S")
    public void scheduledTask() {
        List<EventOutbox> eventOutboxes = eventOutBoxEventService.findAllEventOutbox();
        List<Long> deletionEventOutBoxEntityIds = new ArrayList<>();
        try {
            for(EventOutbox keyCloakEvent: eventOutboxes) {
                try {
                    KeyCloakEventDTO keyCloakEventDTO = new KeyCloakEventDTO();
                    keyCloakEventDTO.setEventType(keyCloakEvent.getEventType());
                    keyCloakEventDTO.setEvent_time(keyCloakEvent.getEventTime());
                    keyCloakEventDTO.setId(keyCloakEvent.getId());
                    kafkaPublisher.publishEvent(keyCloakEventDTO).get();
                    deletionEventOutBoxEntityIds.add(keyCloakEvent.getId());
                } catch (Exception e) {
                    log.error("Error during publish kafka event {}, {}", keyCloakEvent, e.getCause());
                    throw e;
                }
            }
        } catch (Exception e) {
            log.warn("Kafka event publish is stopped because of exception", e);
        }

        eventOutBoxEventService.deleteProcessedEvents(deletionEventOutBoxEntityIds);
    }
}
