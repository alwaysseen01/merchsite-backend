package com.github.alwaysseen.merchsite.payment.paypal.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalOrderIntent;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalPurchaseUnit;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PaymentSource;
import lombok.Data;

import java.util.List;

@Data
public class PayPalOrderRequest {
    @JsonProperty("intent")
    private PayPalOrderIntent orderIntent;

    @JsonProperty("purchase_units")
    private List<PayPalPurchaseUnit> purchaseUnits;

    @JsonProperty("payment_source")
    private PaymentSource paymentSource;
}
