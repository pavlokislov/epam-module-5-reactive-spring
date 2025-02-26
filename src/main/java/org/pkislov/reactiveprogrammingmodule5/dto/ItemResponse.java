package org.pkislov.reactiveprogrammingmodule5.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ItemResponse {

    @JsonProperty("Items")
    private List<Items> items;

}

