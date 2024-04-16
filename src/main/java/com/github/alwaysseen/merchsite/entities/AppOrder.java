package com.github.alwaysseen.merchsite.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private PayPalOrderStatus paypalOrderStatus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    @JsonIgnore
    @OneToMany(mappedBy = "appOrder")
    private List<OrderItem> orderItems;
}
