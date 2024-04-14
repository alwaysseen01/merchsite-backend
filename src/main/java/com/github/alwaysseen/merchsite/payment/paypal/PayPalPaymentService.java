package com.github.alwaysseen.merchsite.payment.paypal;

import com.nimbusds.jose.shaded.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class PayPalPaymentService {
    private final String CLIENT_ID = "Af-uo24kik7c0d0tDqUan8uev07dhR8PaLqXjfOG354sZSgj_TRLM98-sbRUwcl7kg1-5s1NH8mGBjPf";
    private final String CLIENT_SECRET = "EPpzaXFjWJ6U4wJ-YK2Qit4hgvZaWvryazPK5USFW_WOD0D6GmG7fLQC3_g3Z9dsQMuSE05S4E15qCxL";
    @Getter
    @Setter
    private String accessToken;

    public void getPayPalAccessToken(){
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
            setAccessToken(response.getBody().getAccessToken());
        } else {
            setAccessToken(null);
        }
    }

    public boolean isTokenExpired(){
        try{
            if(getAccessToken() != null){
                Claims claims = Jwts.parserBuilder().build().parseClaimsJws(getAccessToken()).getBody();
                long expirationTime = claims.getExpiration().getTime();
                long currentTime = System.currentTimeMillis();
                return currentTime >= expirationTime;
            } else {
                return true;
            }
        } catch (ExpiredJwtException e){
            return true;
        }
    }
}
