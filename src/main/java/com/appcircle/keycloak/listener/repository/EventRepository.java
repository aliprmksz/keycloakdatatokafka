
package com.appcircle.keycloak.listener.repository;


import com.appcircle.keycloak.listener.model.EventQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends JpaRepository<EventQuery, Long> {

}

