package com.github.alwaysseen.merchsite.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "app_order")
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paypal_payment_id", referencedColumnName = "id")
    private PayPalPayment payPalPayment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "eth_payment_id", referencedColumnName = "id")
    private EthPayment ethPayment;

    @JsonIgnore
    @OneToMany(mappedBy = "appOrder")
    private List<OrderItem> orderItems;
}
