package org.pkislov.reactiveprogrammingmodule5.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Items {

    @JsonProperty("Item")
    private ItemDto item;

}
