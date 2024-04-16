package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PayPalCapturePurchaseUnit {
    @JsonProperty("payments")
    private PayPalPayment payments;
}
