package com.github.alwaysseen.merchsite.payment.paypal.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalAmount;
import lombok.Data;

@Data
public class PayPalRefundRequest {
    @JsonProperty("amount")
    private PayPalAmount amount;
}
