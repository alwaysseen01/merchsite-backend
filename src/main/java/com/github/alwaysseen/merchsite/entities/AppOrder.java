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
public class AppOrder {
    @Id
    @SequenceGenerator(
            name = "orderSequence",
            sequenceName = "orderSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orderSequence"
    )
    private Integer id;
    private String paypalOrderId;
    private String paypalOrderStatus;

    public AppOrder(String paypalOrderId, String paypalOrderStatus) {
        this.paypalOrderId = paypalOrderId;
        this.paypalOrderStatus = paypalOrderStatus;
    }
}
