package com.github.alwaysseen.merchsite.payment.paypal.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalLink;
import lombok.Data;

import java.util.List;

@Data
public class PayPalOrderResponse {
    @JsonProperty("id")
    private String orderId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("links")
    private List<PayPalLink> links;
}
