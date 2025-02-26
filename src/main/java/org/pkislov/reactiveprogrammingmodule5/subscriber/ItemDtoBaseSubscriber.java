package org.pkislov.reactiveprogrammingmodule5.subscriber;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.pkislov.reactiveprogrammingmodule5.dto.ItemDto;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

@Slf4j
public class ItemDtoBaseSubscriber extends BaseSubscriber<ItemDto> {

    public static final int NUMBER_OF_REQUEST = 20;
    int itemsProcessed = 0;

    @Override
    protected void hookOnSubscribe(@NonNull Subscription subscription) {
        request(NUMBER_OF_REQUEST);
    }

    @Override
    protected void hookOnNext(@NonNull ItemDto itemDto) {
        itemsProcessed++;
        if (itemsProcessed % NUMBER_OF_REQUEST == 0) {
            log.info("Processed %s items, requesting next %s items".formatted(NUMBER_OF_REQUEST, NUMBER_OF_REQUEST));
            request(NUMBER_OF_REQUEST);
        }
    }
}
