package com.github.alwaysseen.merchsite.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayPalPayment {
    @Id
    @SequenceGenerator(
            name = "paypalPaymentSequence",
            sequenceName = "paypalPaymentSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "paypalPaymentSequence"
    )
    private int id;

    private String paypalOrderId;

    @Enumerated(EnumType.STRING)
    private PayPalOrderStatus paypalOrderStatus;

    @JsonIgnore
    @OneToOne(mappedBy = "payPalPayment")
    private AppOrder order;
}
