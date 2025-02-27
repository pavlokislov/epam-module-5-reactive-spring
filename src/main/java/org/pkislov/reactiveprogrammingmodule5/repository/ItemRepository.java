package org.pkislov.reactiveprogrammingmodule5.repository;

import org.pkislov.reactiveprogrammingmodule5.enity.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveCrudRepository<Item, Integer> {

    Mono<Item> findByName(String name);


}
