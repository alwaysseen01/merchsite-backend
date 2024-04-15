package com.github.alwaysseen.merchsite.payment.paypal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalOrderRequest;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalAccessTokenResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalRefundResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(PayPalPaymentService.class);

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

        ResponseEntity<PayPalAccessTokenResponse> response = restTemplate.postForEntity(url, requestEntity, PayPalAccessTokenResponse.class);
        if(response.getStatusCode().is2xxSuccessful()){
            setAccessToken(response.getBody().getAccessToken());
        } else {
            setAccessToken(null);
        }
    }

    public boolean isTokenExpired(){
        try{
            if(getAccessToken() != null){
                return false;
//                Claims claims = Jwts.parserBuilder().build().parseClaimsJws(getAccessToken()).getBody();
//                long expirationTime = claims.getExpiration().getTime();
//                long currentTime = System.currentTimeMillis();
//                return currentTime >= expirationTime;
            } else {
                return true;
            }
        } catch (ExpiredJwtException e){
            return true;
        }
    }

    public PayPalOrderResponse createOrder(PayPalOrderRequest request) throws JsonProcessingException {
        logger.info("METHOD createOrder CALLED");
        String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("CHECKING ACCESS TOKEN");
        if(isTokenExpired()){
            logger.info("MISSING TOKEN, GENERATING NEW");
            getPayPalAccessToken();
        }
        logger.info("ACCESS TOKEN "+getAccessToken());
        headers.setBearerAuth(getAccessToken());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<PayPalOrderResponse> response = restTemplate.postForEntity(url, requestEntity, PayPalOrderResponse.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            return null;
        }
    }

    public PayPalRefundResponse refund(String orderId){
        String url = "https://api-m.paypal.com/v2/payments/captures/"+orderId+"/refund";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(getAccessToken() == null || isTokenExpired()){
            getPayPalAccessToken();
        }
        headers.setBearerAuth(getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<PayPalRefundResponse> response = restTemplate.postForEntity(url, requestEntity, PayPalRefundResponse.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            return null;
        }
    }
}
