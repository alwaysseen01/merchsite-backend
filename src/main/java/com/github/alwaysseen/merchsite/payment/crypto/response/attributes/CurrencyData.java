package com.github.alwaysseen.merchsite.payment.crypto.response.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyData {
    @JsonProperty("ETH")
    private CurrencyEth eth;
}
