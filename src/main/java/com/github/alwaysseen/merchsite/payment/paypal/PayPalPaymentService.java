package com.github.alwaysseen.merchsite.payment.paypal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalOrderRequest;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalRefundRequest;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalAccessTokenResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderCaptureResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalRefundResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalGetCaptureResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PayPalPaymentService {
    private final String CLIENT_ID = "Af-uo24kik7c0d0tDqUan8uev07dhR8PaLqXjfOG354sZSgj_TRLM98-sbRUwcl7kg1-5s1NH8mGBjPf";
    private final String CLIENT_SECRET = "EPpzaXFjWJ6U4wJ-YK2Qit4hgvZaWvryazPK5USFW_WOD0D6GmG7fLQC3_g3Z9dsQMuSE05S4E15qCxL";
    private final Logger logger = LoggerFactory.getLogger(PayPalPaymentService.class);


    private String accessToken;
    private int expiresIn;

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
            accessToken = response.getBody().getAccessToken();
            expiresIn = response.getBody().getExpiresIn();
        } else {
            accessToken = null;
            expiresIn = 0;
        }
    }

    public boolean isTokenExpired(){
        try{
            if(accessToken != null){
                long expirationTime = expiresIn;
                long currentTime = System.currentTimeMillis()/1000;
                return currentTime >= expirationTime;
            } else {
                return true;
            }
        } catch (ExpiredJwtException e){
            return true;
        }
    }

    public PayPalOrderCaptureResponse capturePayment(String orderId){
        String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders/"+orderId+"/capture";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(isTokenExpired()){
            getPayPalAccessToken();
        }
        headers.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<PayPalOrderCaptureResponse> response = restTemplate.postForEntity(url, requestEntity, PayPalOrderCaptureResponse.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            return null;
        }
    }

    public PayPalGetCaptureResponse capturedPaymentDetails(String capture_id){
        String url = "https://api-m.sandbox.paypal.com/v2/payments/captures/"+capture_id;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(isTokenExpired()){
            getPayPalAccessToken();
        }
        headers.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<PayPalGetCaptureResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PayPalGetCaptureResponse.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            return null;
        }
    }

    public PayPalOrderResponse createOrder(PayPalOrderRequest request) throws JsonProcessingException {
        String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(isTokenExpired()){
            getPayPalAccessToken();
        }
        headers.setBearerAuth(accessToken);

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

    public PayPalRefundResponse refund(String capture_id, PayPalRefundRequest request) throws JsonProcessingException {
        logger.info("SERVICE METHOD REFUND");
        String url = "https://api-m.sandbox.paypal.com/v2/payments/captures/"+capture_id+"/refund";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(isTokenExpired()){
            getPayPalAccessToken();
        }
        headers.setBearerAuth(accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<PayPalRefundResponse> response = restTemplate.postForEntity(url, requestEntity, PayPalRefundResponse.class);
        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            return null;
        }
    }
}
