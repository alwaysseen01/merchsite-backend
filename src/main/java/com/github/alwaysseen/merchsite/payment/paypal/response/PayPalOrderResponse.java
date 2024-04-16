package com.github.alwaysseen.merchsite.payment.paypal.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.entities.PayPalOrderStatus;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalLink;
import lombok.Data;

import java.util.List;

@Data
public class PayPalOrderResponse {
    @JsonProperty("id")
    private String orderId;

    @JsonProperty("status")
    private PayPalOrderStatus status;

    @JsonProperty("links")
    private List<PayPalLink> links;
}
