package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.entities.PayPalCaptureStatus;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalAmount;
import lombok.Data;

import java.util.List;

@Data
public class PayPalCapture {
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private PayPalCaptureStatus status;

    @JsonProperty("amount")
    private PayPalAmount amount;

    @JsonProperty("links")
    private List<PayPalLink> links;
}
