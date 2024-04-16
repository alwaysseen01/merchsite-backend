package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.entities.PayPalCaptureStatus;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalAmount;
import lombok.Data;

@Data
public class PayPalGetCaptureResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private PayPalCaptureStatus status;

    @JsonProperty("seller_receivable_breakdown")
    private PayPalSellerReceivableBreakdown sellerReceivableBreakdown;
}
