package com.appcircle.keycloak.listener.service;

import com.appcircle.keycloak.listener.dto.KeyCloakEventDTO;
import com.appcircle.keycloak.listener.model.AdminEventQuery;
import com.appcircle.keycloak.listener.model.EventOutbox;
import com.appcircle.keycloak.listener.model.EventQuery;
import com.appcircle.keycloak.listener.model.KeyCloakAdminEventEntity;
import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import com.appcircle.keycloak.listener.repository.AdminEventRepository;
import com.appcircle.keycloak.listener.repository.EventOutboxRepository;
import com.appcircle.keycloak.listener.repository.EventRepository;
import com.appcircle.keycloak.listener.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventOutboxService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AdminEventRepository adminEventRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventOutboxRepository eventOutboxRepository;


    public Optional<EventQuery> queryLatestEvent() {
        return eventRepository.findAll(
                PageRequest.of(0, 1,
                        Sort.by(Sort.Direction.DESC, "latestQueryEventTimestamp"))).stream().findFirst();

    }

    public List<EventOutbox> findAllEventOutbox() {
        return eventOutboxRepository.findAll(Sort.by(Sort.Direction.ASC, "eventTime"));
    }

    public Optional<AdminEventQuery> queryLatestAdminEvent() {
        return adminEventRepository.findAll(
                PageRequest.of(0, 1,
                        Sort.by(Sort.Direction.DESC, "latestQueryEventTimestamp"))).stream().findFirst();

    }


    public List<KeyCloakEventEntity> queryEventEntityByEventTime(Optional<EventQuery> eventQuery)  {

        if(eventQuery.isPresent()) {
            return eventQuery.map(EventQuery::getLatestQueryEventTimestamp)
                    .map(eventTime -> eventsRepository.queryEventEntityByEventTime(Optional.ofNullable(eventTime))).get();
        } else {
            return eventsRepository.queryEventEntityByEventTime(Optional.empty());
        }
    }

    public List<KeyCloakAdminEventEntity> queryAdminEventEntityByEventTime(Optional<AdminEventQuery> eventQuery)  {

        if(eventQuery.isPresent()) {
            return eventQuery.map(AdminEventQuery::getLatestQueryEventTimestamp)
                    .map(eventTime -> eventsRepository.queryAdminEventEntityByEventTime(Optional.ofNullable(eventTime))).get();
        } else {
            return eventsRepository.queryAdminEventEntityByEventTime(Optional.empty());
        }
    }

    public Optional<EventQuery> saveEventOutboxWithEventQuery(KeyCloakEventDTO keyCloakEventDTO,
                                                              Optional<EventQuery> eventQuery, Optional<Long> processedLatestEvent) {
        EventOutbox eventOutbox = new EventOutbox();
        eventOutbox.setEventTime(keyCloakEventDTO.getEvent_time());
        eventOutbox.setEventType(keyCloakEventDTO.getEventType());
        eventOutboxRepository.save(eventOutbox);
        return updateEventEntity(eventQuery, processedLatestEvent);
    }

    public Optional<AdminEventQuery> saveEventOutboxWithAdminEventQuery(KeyCloakEventDTO keyCloakEventDTO,
                                Optional<AdminEventQuery> adminEventQuery, Optional<Long> processedLatestAdminEvent) {
        EventOutbox eventOutbox = new EventOutbox();
        eventOutbox.setEventTime(keyCloakEventDTO.getEvent_time());
        eventOutbox.setEventType(keyCloakEventDTO.getEventType());
        eventOutboxRepository.save(eventOutbox);
        return updateAdminEventEntity(adminEventQuery, processedLatestAdminEvent);
    }


    public Optional<EventQuery> updateEventEntity(Optional<EventQuery> eventQuery, Optional<Long> processedLatestEvent) {
        List<EventQuery> eventEntity = new ArrayList<>();
        eventQuery.ifPresentOrElse(eventQueryValue -> {
            eventQueryValue.setLatestQueryEventTimestamp(processedLatestEvent.get());
            eventEntity.add(eventRepository.save(eventQueryValue));
        }, () -> processedLatestEvent.ifPresent(processedLatestEventValue -> {
            EventQuery query = new EventQuery();
            query.setLatestQueryEventTimestamp(processedLatestEventValue);
            eventEntity.add(eventRepository.save(query));
        }));

        return Optional.ofNullable(eventEntity.get(0));
    }

    public Optional<AdminEventQuery> updateAdminEventEntity( Optional<AdminEventQuery> adminEventQuery, Optional<Long> processedLatestAdminEvent) {
        List<AdminEventQuery> eventEntity = new ArrayList<>();
        adminEventQuery.ifPresentOrElse(eventQueryValue -> {
            eventQueryValue.setLatestQueryEventTimestamp(processedLatestAdminEvent.get());
            eventEntity.add(adminEventRepository.save(eventQueryValue));
        }, () -> processedLatestAdminEvent.ifPresent(processedLatestEventValue -> {
            AdminEventQuery query = new AdminEventQuery();
            query.setLatestQueryEventTimestamp(processedLatestEventValue);
            eventEntity.add(adminEventRepository.save(query));
        }));
        return Optional.ofNullable(eventEntity.get(0));
    }

    public void deleteProcessedEvents(List<Long> deletionEventOutBoxEntityIds) {
        Iterable<Long> deletionEventOutBoxEntityIdsIterable = deletionEventOutBoxEntityIds;
        eventOutboxRepository.deleteAllByIdInBatch(deletionEventOutBoxEntityIdsIterable);
    }
}
