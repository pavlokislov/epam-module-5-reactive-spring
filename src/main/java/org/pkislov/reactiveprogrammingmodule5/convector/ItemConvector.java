package org.pkislov.reactiveprogrammingmodule5.convector;

import org.pkislov.reactiveprogrammingmodule5.dto.ItemDto;
import org.pkislov.reactiveprogrammingmodule5.enity.Item;

public class ItemConvector {

    public static ItemDto convert(Item item) {
        if (item == null) {
            return new ItemDto();
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        return itemDto;
    }
}
