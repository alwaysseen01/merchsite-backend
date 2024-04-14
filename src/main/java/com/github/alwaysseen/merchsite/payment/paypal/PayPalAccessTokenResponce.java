package com.github.alwaysseen.merchsite.payment.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayPalAccessTokenResponce {
    @JsonProperty("scope")
    private String scope;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("tokenType")
    private String tokenType;

    @JsonProperty("app_id")
    private String app_id;

    @JsonProperty("expires_in")
    private int expires_in;

    @JsonProperty("nonce")
    private String nonce;
}
