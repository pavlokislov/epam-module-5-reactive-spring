package org.pkislov.reactiveprogrammingmodule5.config;


import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
class DatabaseInitializer {

    @Value("${spring.r2dbc.isCreateTable:false}")
    private Boolean isCreateTable;

    private final ConnectionFactory connectionFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void handleContextRefresh() {
        if (isCreateTable) {
            createTable().block();
        }
    }

    private Mono<Void> createTable() {
        log.info("Creating item table");
        DatabaseClient client = DatabaseClient.create(connectionFactory);
        return client.sql("CREATE TABLE IF NOT EXISTS item(id SERIAL PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL)")
                .fetch()
                .rowsUpdated()
                .then();
    }
}
