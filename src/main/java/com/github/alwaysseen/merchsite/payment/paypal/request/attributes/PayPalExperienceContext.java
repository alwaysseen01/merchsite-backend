package com.github.alwaysseen.merchsite.payment.paypal.request.attributes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayPalExperienceContext {
    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("landing_page")
    private PaymentLandingPage landingPage;

    @JsonProperty("return_url")
    private String returnUrl;

    @JsonProperty("cancel_url")
    private String cancelUrl;
}
