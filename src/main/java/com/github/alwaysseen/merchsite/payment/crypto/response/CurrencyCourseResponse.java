package com.github.alwaysseen.merchsite.payment.crypto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alwaysseen.merchsite.payment.crypto.response.attributes.CurrencyData;
import com.github.alwaysseen.merchsite.payment.crypto.response.attributes.CurrencyStatus;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CurrencyCourseResponse {
    @JsonProperty("data")
    private CurrencyData data;

    @JsonProperty("status")
    private CurrencyStatus status;
}
