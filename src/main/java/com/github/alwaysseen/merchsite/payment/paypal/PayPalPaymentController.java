package com.github.alwaysseen.merchsite.payment.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalOrderRequest;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalAppContext;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalOrderIntent;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PayPalPurchaseUnit;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.PaymentLandingPage;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalLink;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/paypal")
@RequiredArgsConstructor
public class PayPalPaymentController {
    @Autowired
    private final PayPalPaymentService service;
    @Autowired
    private final OrderRepository orderRepository;

    private final Logger logger = LoggerFactory.getLogger(PayPalPaymentController.class);

    record OrderRequest(
            @JsonProperty("intent")
            PayPalOrderIntent orderIntent,
            @JsonProperty("purchase_units")
            List<PayPalPurchaseUnit> purchaseUnits
    ){}

    @PostMapping("/checkout")
    public ResponseEntity<List<PayPalLink>> checkout(@RequestBody OrderRequest orderRequest){
        PayPalOrderRequest request = new PayPalOrderRequest();
        request.setPurchaseUnits(orderRequest.purchaseUnits());
        request.setOrderIntent(orderRequest.orderIntent());
        PayPalAppContext context = new PayPalAppContext();
        context.setBrandName("Merchsite");
        context.setLandingPage(PaymentLandingPage.BILLING);
        //just set it for the test :)
        context.setReturnUrl("https://google.com");
        request.setContext(context);
        try {
            PayPalOrderResponse response = service.createOrder(request);
            if(response != null){
                AppOrder order = new AppOrder();
                order.setPaypalOrderId(response.getOrderId());
                order.setPaypalOrderStatus(response.getStatus());
                orderRepository.save(order);
                return new ResponseEntity<>(response.getLinks(), HttpStatus.OK);
            } else {
                logger.warn("YOU FUCKED UP AGAIN!");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
