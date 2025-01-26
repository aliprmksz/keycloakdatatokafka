package com.appcircle.keycloak.listener.task;

import com.appcircle.keycloak.listener.kafka.KafkaPublisher;
import com.appcircle.keycloak.listener.model.EventQuery;
import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import com.appcircle.keycloak.listener.service.EventQueryService;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventQuerySchedulerTask {

    @Autowired
    private EventQueryService eventQueryService;

    @Autowired
    private KafkaPublisher kafkaPublisher;


    @Scheduled(cron = "*/15 * * * * *")
    @SchedulerLock(name = "EventQuerySchedulerTask.scheduledTask", lockAtLeastFor = "PT10S", lockAtMostFor = "PT15S")
    public void scheduledTask() {
        Optional<EventQuery> latestEventQuery = eventQueryService.queryLatestEvent();
        List<KeyCloakEventEntity> keyCloakEventEntities = eventQueryService.queryEventEntityByEventTime(latestEventQuery);
        Map<String, Optional<Long>>  processedEventTime  = new HashMap<>();
        processedEventTime.put("processedLatestEventQuery", latestEventQuery.map(EventQuery::getLatestQueryEventTimestamp)
                .or(()->Optional.empty()));
        try {
            for(KeyCloakEventEntity keyCloakEventEntity: keyCloakEventEntities) {
                try {
                    kafkaPublisher.publishEvent(keyCloakEventEntity).get();
                    processedEventTime.put("processedLatestEventQuery", Optional.ofNullable(keyCloakEventEntity.getEvent_time()));
                } catch (Exception e) {
                    log.error("Error during publish kafka event {}, {}", keyCloakEventEntity, e.getCause());
                    throw e;
                }
            }
        } catch (Exception e) {
            log.warn("Kafka event publish is stopped because of exception", e);
        }

        eventQueryService.updateEntity(latestEventQuery, processedEventTime.get("processedLatestEventQuery"));
    }
}
