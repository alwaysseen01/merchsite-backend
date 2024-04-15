package com.github.alwaysseen.merchsite.payment.paypal.request.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalPurchaseUnit {
    @JsonProperty("amount")
    private PayPalAmount amount;
}
