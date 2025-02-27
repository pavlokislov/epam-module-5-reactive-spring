package org.pkislov.reactiveprogrammingmodule5.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pkislov.reactiveprogrammingmodule5.enity.Item;
import org.pkislov.reactiveprogrammingmodule5.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final @NonNull ItemRepository itemRepository;

    @Transactional
    public Mono<Item> createItem(String name) {
        Item item = new Item(name);
        return itemRepository.save(item);
    }

    @Transactional
    public Mono<Item> findItem(String name) {
        return itemRepository.findByName(name);
    }

    @Transactional
    public Flux<Item> createItems(List<String> names) {
        List<Item> items = names.stream()
                .map(Item::new)
                .toList();
        return itemRepository.saveAll(items);
    }

}
