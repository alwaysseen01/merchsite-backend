package com.github.alwaysseen.merchsite.payment.paypal.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalLink;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalRefundStatus;
import lombok.Data;

import java.util.List;

@Data
public class PayPalRefundResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private PayPalRefundStatus status;

    @JsonProperty("links")
    private List<PayPalLink> links;
}
