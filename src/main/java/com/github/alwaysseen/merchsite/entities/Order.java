package com.github.alwaysseen.merchsite.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @SequenceGenerator(
            name = "userSequence",
            sequenceName = "userSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userSequence"
    )
    private Integer id;
    private String paypalOrderId;
    private String paypalOrderStatus;

    public Order(String paypalOrderId, String paypalOrderStatus) {
        this.paypalOrderId = paypalOrderId;
        this.paypalOrderStatus = paypalOrderStatus;
    }
}
