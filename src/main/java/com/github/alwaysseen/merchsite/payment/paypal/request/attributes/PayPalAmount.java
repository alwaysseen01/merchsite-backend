package com.github.alwaysseen.merchsite.payment.paypal.request.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalAmount {
    @JsonProperty("value")
    private String value;

    @JsonProperty("currency_code")
    private String currencyCode;
}
