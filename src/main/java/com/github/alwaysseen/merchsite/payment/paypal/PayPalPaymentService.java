package com.github.alwaysseen.merchsite.payment.paypal;

import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PayPalPaymentService {
    private final String CLIENT_ID = "Af-uo24kik7c0d0tDqUan8uev07dhR8PaLqXjfOG354sZSgj_TRLM98-sbRUwcl7kg1-5s1NH8mGBjPf";
    private final String CLIENT_SECRET = "EPpzaXFjWJ6U4wJ-YK2Qit4hgvZaWvryazPK5USFW_WOD0D6GmG7fLQC3_g3Z9dsQMuSE05S4E15qCxL";

    public String getAccessToken(){
        String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<PayPalAccessTokenResponce> response = restTemplate.postForEntity(url, requestEntity, PayPalAccessTokenResponce.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody().getAccessToken();
        } else {
            return null;
        }
    }
}
