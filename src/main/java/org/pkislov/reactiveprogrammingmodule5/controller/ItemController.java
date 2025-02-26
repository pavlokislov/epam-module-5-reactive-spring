package org.pkislov.reactiveprogrammingmodule5.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pkislov.reactiveprogrammingmodule5.convector.ItemConvector;
import org.pkislov.reactiveprogrammingmodule5.dto.ItemDto;
import org.pkislov.reactiveprogrammingmodule5.service.ItemService;
import org.pkislov.reactiveprogrammingmodule5.service.SetupService;
import org.pkislov.reactiveprogrammingmodule5.subscriber.ItemDtoBaseSubscriber;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController(value = "/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final SetupService webClientService;
    private final static ItemDtoBaseSubscriber ITEM_DTO_BASE_SUBSCRIBER = new ItemDtoBaseSubscriber();

    @GetMapping
    public Flux<ItemDto> getItems(@RequestParam String keyword) {
        log.info("Getting items for {}", keyword);
        return webClientService.getItems(keyword);
    }

    @GetMapping("/backpressure")
    public void getItemsWithBackpressure(@RequestParam String keyword) {
        log.info("Getting items for {}", keyword);
        var items = webClientService.getItems(keyword);
        items.subscribe(ITEM_DTO_BASE_SUBSCRIBER);
    }

    @GetMapping("/backpressure2")
    public Flux<ItemDto> getItemsWithBackpressure2(@RequestParam String keyword) {
        log.info("Getting items for {}", keyword);
        return webClientService.getItems(keyword)
                .onBackpressureBuffer(20);
    }

    @PostMapping({"{itemName}"})
    public Mono<ResponseEntity<ItemDto>> createItem(@PathVariable String itemName) {
        log.info("Creating item {}", itemName);
        return itemService.createItem(itemName)
                .map(ItemConvector::convert)
                .map(item -> new ResponseEntity<>(item, HttpStatus.CREATED))
                .doOnError(exception -> log.error(exception.getMessage(), exception))
                .onErrorResume(DuplicateKeyException.class, exception -> Mono.just(new ResponseEntity<>(HttpStatus.CONFLICT)))
                .doOnSuccess(response -> log.info("Item {} created", itemName));
    }

    @GetMapping("/backpressure/items")
    public void getItemsWithBackpressure() {
        Flux<ItemDto> itemDtoFlux = itemService.getItems()
                .map(ItemConvector::convert)
                .doOnError(exception -> log.error(exception.getMessage(), exception))
                .onErrorResume(DuplicateKeyException.class, exception -> Mono.just(new ItemDto("Error DuplicateKeyException %s".formatted(exception.getMessage()))));
        itemDtoFlux.subscribe(ITEM_DTO_BASE_SUBSCRIBER);

    }
}
