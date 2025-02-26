package org.pkislov.reactiveprogrammingmodule5.service;

import lombok.extern.slf4j.Slf4j;
import org.pkislov.reactiveprogrammingmodule5.dto.ItemDto;
import org.pkislov.reactiveprogrammingmodule5.dto.ItemResponse;
import org.pkislov.reactiveprogrammingmodule5.dto.Items;
import org.pkislov.reactiveprogrammingmodule5.subscriber.ItemDtoBaseSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class SetupService {

    private final WebClient webClient;

    @Value("${webservice.application.id}")
    private String applicationId;

    public SetupService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://app.rakuten.co.jp/services/").build();
    }

    public Flux<ItemDto> getItems(@RequestParam String keyword) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/IchibaItem/Search/20220601")
                        .queryParam("format", "json")
                        .queryParam("keyword", keyword)
                        .queryParam("applicationId", applicationId)
                        .build())
                .retrieve()
                .bodyToFlux(ItemResponse.class)
                .flatMapIterable(ItemResponse::getItems)
                .map(Items::getItem)
                .doOnRequest(request -> log.info("Request: {}", request))
                .doOnNext(item -> log.info("Processing next item from Rakuten.com {}", item));
    }
}