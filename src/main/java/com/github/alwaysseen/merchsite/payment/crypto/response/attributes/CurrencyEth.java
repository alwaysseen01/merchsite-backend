package com.github.alwaysseen.merchsite.payment.crypto.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyEth {
    @JsonProperty("quote")
    private CurrencyQuote quote;
}
