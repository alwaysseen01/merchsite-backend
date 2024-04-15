package com.github.alwaysseen.merchsite.payment.paypal.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalLink {
    @JsonProperty("href")
    private String href;

    @JsonProperty("rel")
    private String rel;

    @JsonProperty("method")
    private String method;
}
