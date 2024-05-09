package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalCapturePurchaseUnit {
    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("payments")
    private PayPalPaymentResponse payments;
}
