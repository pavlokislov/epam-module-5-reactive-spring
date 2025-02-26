package org.pkislov.reactiveprogrammingmodule5.enity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@PersistenceConstructor))
@Table("item")
public class Item {

    @Id
    private Long id;
    private String name;

    public Item(String name) {
        this.name = name;
    }
}