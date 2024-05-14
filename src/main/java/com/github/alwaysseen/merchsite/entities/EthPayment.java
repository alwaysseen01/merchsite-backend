package com.github.alwaysseen.merchsite.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ethPayment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EthPayment {
    @Id
    @SequenceGenerator(
            name = "ethPaymentSequence",
            sequenceName = "ethPaymentSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ethPaymentSequence"
    )
    private int id;

    private String txHash;

    @Enumerated(EnumType.STRING)
    private EthPaymentStatus status;

    @JsonIgnore
    @OneToOne(mappedBy = "ethPayment")
    private AppOrder order;
}
