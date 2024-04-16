package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalAmount;
import lombok.Data;

@Data
public class PayPalSellerReceivableBreakdown {
    @JsonProperty("gross_amount")
    private PayPalAmount grossAmount;

    @JsonProperty("paypal_fee")
    private PayPalAmount paypalFee;

    @JsonProperty("net_amount")
    private PayPalAmount netAmount;
}
