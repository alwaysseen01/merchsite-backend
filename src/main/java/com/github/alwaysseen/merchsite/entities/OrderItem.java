package com.github.alwaysseen.merchsite.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @SequenceGenerator(
            name = "orderItemSequence",
            sequenceName = "orderItemSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orderItemSequence"
    )
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private AppOrder appOrder;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
