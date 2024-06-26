package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PayPalPaymentResponse {
    @JsonProperty("captures")
    private List<PayPalCapture> captures;
}
