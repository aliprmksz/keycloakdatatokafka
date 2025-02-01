package com.appcircle.keycloak.listener.task;

import com.appcircle.keycloak.listener.model.AdminEventQuery;
import com.appcircle.keycloak.listener.model.EventQuery;
import com.appcircle.keycloak.listener.model.KeyCloakAdminEventEntity;
import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import com.appcircle.keycloak.listener.service.EventOutboxService;
import com.appcircle.keycloak.listener.dto.KeyCloakEventDTO;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class EventOutboxPrepareSchedulerTask {

    @Autowired
    private EventOutboxService eventOutboxService;

    //@Scheduled(cron = "*/7 * * * * *")
    //@SchedulerLock(name = "EventOutboxPrepareSchedulerTask.scheduledTask", lockAtLeastFor = "PT5S", lockAtMostFor = "PT7S")
    public void scheduledTask() {
        Optional<EventQuery> latestEventQuery = eventOutboxService.queryLatestEvent();
        Optional<AdminEventQuery> latestAdminEventQuery = eventOutboxService.queryLatestAdminEvent();
        List<KeyCloakEventEntity> keyCloakEventEntities = eventOutboxService.queryEventEntityByEventTime(latestEventQuery);

        List<KeyCloakAdminEventEntity> keyCloakAdminEventEntities = eventOutboxService
                .queryAdminEventEntityByEventTime(latestAdminEventQuery);

        List<KeyCloakEventDTO> keyCloakEventDTOS = new ArrayList<>();

        for(KeyCloakEventEntity keyCloakEventEntity : keyCloakEventEntities) {
            KeyCloakEventDTO eventDTO = new KeyCloakEventDTO();
            eventDTO.setEvent_time(keyCloakEventEntity.getEvent_time());
            eventDTO.setEventType(keyCloakEventEntity.getType());
            eventDTO.setAdminEvent(false);
            keyCloakEventDTOS.add(eventDTO);
        };

        for(KeyCloakAdminEventEntity keyCloakAdminEventEntity : keyCloakAdminEventEntities) {
            KeyCloakEventDTO eventDTO = new KeyCloakEventDTO();
            eventDTO.setEvent_time(keyCloakAdminEventEntity.getAdmin_event_time());
            eventDTO.setEventType(keyCloakAdminEventEntity.getOperation_type());
            eventDTO.setAdminEvent(true);
            keyCloakEventDTOS.add(eventDTO);
        };


        keyCloakEventDTOS = keyCloakEventDTOS.stream().sorted(Comparator.comparing(KeyCloakEventDTO::getEvent_time)).toList();

        for(KeyCloakEventDTO keyCloakEventDTO : keyCloakEventDTOS) {
            if (!keyCloakEventDTO.isAdminEvent()) {
                latestEventQuery = eventOutboxService.saveEventOutboxWithEventQuery(keyCloakEventDTO,
                        latestEventQuery, Optional.ofNullable(keyCloakEventDTO.getEvent_time()));
            } else {
                latestAdminEventQuery = eventOutboxService.saveEventOutboxWithAdminEventQuery(keyCloakEventDTO,
                        latestAdminEventQuery, Optional.ofNullable(keyCloakEventDTO.getEvent_time()));
            }
        }


    }
}
