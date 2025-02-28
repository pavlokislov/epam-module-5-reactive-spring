package org.pkislov.reactiveprogrammingmodule5.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pkislov.reactiveprogrammingmodule5.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
class DatabaseInitializer {

    @Value("${spring.r2dbc.isCreateTable:false}")
    private Boolean isCreateTable;

    private final ConnectionFactory connectionFactory;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final ItemService itemService;

    @EventListener(ApplicationReadyEvent.class)
    public void handleContextRefresh() {
        if (isCreateTable) {
            log.info("Creating table");
            createTable().block();
            Flux.fromIterable(loadItems())
                    .log()
                    .limitRate(20, 0)
                    .doOnComplete(() -> log.info("Items populated"))
                    .doOnError(e -> log.error("Items populated failed %s", e))
                    .map(itemService::createItem)
                    .subscribe(item -> log.info("Item created: {}", item));
            log.info("Items created");
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

    public List<String> loadItems() {
        Resource resource = resourceLoader.getResource("classpath:items.json");
        try (InputStream inputStream = resource.getInputStream()) {
            Map<String, List<String>> data = objectMapper.readValue(inputStream, Map.class);
            return data.get("items");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
