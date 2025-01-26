package com.appcircle.keycloak.listener.service;

import com.appcircle.keycloak.listener.model.EventQuery;
import com.appcircle.keycloak.listener.model.KeyCloakEventEntity;
import com.appcircle.keycloak.listener.repository.EventEntityRepository;
import com.appcircle.keycloak.listener.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventQueryService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventEntityRepository eventEntityRepository;

    public Optional<EventQuery> queryLatestEvent() {
        return eventRepository.findAll(
                PageRequest.of(0, 1,
                        Sort.by(Sort.Direction.DESC, "latestQueryEventTimestamp"))).stream().findFirst();

    }

    public List<KeyCloakEventEntity> queryEventEntityByEventTime(Optional<EventQuery> eventQuery)  {

        if(eventQuery.isPresent()) {
            return eventQuery.map(EventQuery::getLatestQueryEventTimestamp)
                    .map(eventTime -> eventEntityRepository.queryEventEntityByEventTime(Optional.ofNullable(eventTime))).get();
        } else {
            return eventEntityRepository.queryEventEntityByEventTime(Optional.empty());
        }
    }

    public void updateEntity( Optional<EventQuery> eventQuery, Optional<Long> processedLatestEvent) {
        eventQuery.ifPresentOrElse(eventQueryValue -> {
            eventQueryValue.setLatestQueryEventTimestamp(processedLatestEvent.get());
            eventRepository.save(eventQueryValue);
        }, () -> processedLatestEvent.ifPresent(processedLatestEventValue -> {
            EventQuery query = new EventQuery();
            query.setLatestQueryEventTimestamp(processedLatestEventValue);
            eventRepository.save(query);
        }));
    }
}
