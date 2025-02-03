A scheduler polls outbox table and send data to kafka. Sched lock is used for distributed lock and spring-kafka is used for sending data to kafka.

Applying helm charts of postgre, keycloak, zookeeper, kafka will start these applications. helm-chart will create this listener microservice.
