package com.github.alwaysseen.merchsite.payment.paypal.request.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayPalPurchaseUnit {
    @JsonProperty("amount")
    private PayPalAmount amount;
}
