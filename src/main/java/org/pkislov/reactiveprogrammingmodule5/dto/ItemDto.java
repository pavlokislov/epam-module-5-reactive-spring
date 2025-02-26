package org.pkislov.reactiveprogrammingmodule5.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("itemCode")
    private String itemCode;

    @JsonProperty("itemName")
    private String name;

    public ItemDto(String name) {
        this.name = name;
    }
}
