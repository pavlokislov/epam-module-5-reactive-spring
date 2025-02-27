package org.pkislov.reactiveprogrammingmodule5.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pkislov.reactiveprogrammingmodule5.convector.ItemConvector;
import org.pkislov.reactiveprogrammingmodule5.dto.ItemDto;
import org.pkislov.reactiveprogrammingmodule5.service.ItemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ItemRouter {

    private final ItemService itemService;

    @Bean
    public RouterFunction<ServerResponse> itemRoutes() {
        return RouterFunctions.route()
                .GET("/api/v1/item", this::getItems)
                .POST("/api/v1/item/{itemName}", this::createItem)
                .build();
    }

    private Mono<ServerResponse> getItems(ServerRequest request) {
        List<String> keywords = request.queryParams().get("keyword");
        return ServerResponse.ok().body(
                Flux.fromIterable(keywords)
                        .log()
                        .limitRate(20)
                        .flatMap(itemService::findItem)
                        .map(ItemConvector::convert),
                ItemDto.class);
    }

    private Mono<ServerResponse> createItem(ServerRequest request) {
        String itemName = request.pathVariable("itemName");
        log.info("Creating item {}", itemName);
        return itemService.createItem(itemName)
                .map(ItemConvector::convert)
                .flatMap(itemDto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(itemDto))
                .onErrorResume(DuplicateKeyException.class, e -> {
                    log.error("Failed to create item, name is duplicate: {}", itemName);
                    return ServerResponse.status(HttpStatus.CONFLICT).bodyValue("Item already exists!");
                });
    }
}
