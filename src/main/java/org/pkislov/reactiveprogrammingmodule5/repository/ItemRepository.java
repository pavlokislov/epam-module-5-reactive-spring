package org.pkislov.reactiveprogrammingmodule5.repository;

import org.pkislov.reactiveprogrammingmodule5.enity.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, Integer> {
}
