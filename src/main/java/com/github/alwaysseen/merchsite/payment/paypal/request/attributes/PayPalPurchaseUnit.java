package com.github.alwaysseen.merchsite.payment.paypal.request.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayPalPurchaseUnit {
    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("custom_id")
    private String customId;

    @JsonProperty("amount")
    private PayPalAmount amount;
}
