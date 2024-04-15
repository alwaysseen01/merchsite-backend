package com.github.alwaysseen.merchsite.payment.paypal.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class PayPalAccessTokenResponse {
    @JsonProperty("scope")
    private String scope;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("nonce")
    private String nonce;
}
