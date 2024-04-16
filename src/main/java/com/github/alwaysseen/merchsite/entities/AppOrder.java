package com.github.alwaysseen.merchsite.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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
    private PayPalOrderStatus paypalOrderStatus;
    private String paypalCaptureId;
    private PayPalCaptureStatus paypalCaptureStatus;
}
