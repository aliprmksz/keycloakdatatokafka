In this project, a scheduler queries keycloak event_entity and admin_event_entity tables periodically and inserts data into event_outbox table.
Another scheduler polls outbox table and send data to kafka. Sched lock is used for distributed lock and spring-kafka is used for sending data to kafka.
