
package com.appcircle.keycloak.listener.repository;


import com.appcircle.keycloak.listener.model.EventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface EventOutboxRepository extends JpaRepository<EventOutbox, Long> {

}

