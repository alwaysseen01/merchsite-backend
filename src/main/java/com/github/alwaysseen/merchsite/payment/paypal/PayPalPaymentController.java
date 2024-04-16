package com.github.alwaysseen.merchsite.payment.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.OrderItem;
import com.github.alwaysseen.merchsite.entities.PayPalOrderStatus;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalOrderRequest;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalRefundRequest;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.*;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderCaptureResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalRefundResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalLink;
import com.github.alwaysseen.merchsite.repositories.OrderItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api/paypal")
@RequiredArgsConstructor
public class PayPalPaymentController {
    @Autowired
    private final PayPalPaymentService service;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;

    DecimalFormat amountValueFormat = new DecimalFormat("0.00");

    private final Logger logger = LoggerFactory.getLogger(PayPalPaymentController.class);

    @PostMapping("/checkout/{order_id}")
    public ResponseEntity<List<PayPalLink>> checkout(@PathVariable("order_id") Integer appOrderId,
                                                     @AuthenticationPrincipal UserDetails user){
        AppOrder order = orderRepository.findById(appOrderId).get();
        if(order.getUser().getEmail().equals(user.getUsername())){
            PayPalOrderRequest request = new PayPalOrderRequest();

            PayPalExperienceContext context = new PayPalExperienceContext();
            context.setBrandName("Merchsite");
            context.setLandingPage(PaymentLandingPage.GUEST_CHECKOUT);
            context.setReturnUrl("http://localhost:8080/api/paypal/checkout/success");
            context.setCancelUrl("https://github.com");
            request.setPaymentSource(new PaymentSource(new PayPalPaymentSource(context)));

            List<OrderItem> orderItems = orderItemRepository.findByAppOrder(order);
            List<PayPalPurchaseUnit> purchaseUnits = new ArrayList<>();
            for(OrderItem orderItem: orderItems){
                PayPalAmount amount = new PayPalAmount();
                amount.setCurrencyCode("USD");
                amount.setValue(amountValueFormat.format(orderItem.getItem().getPrice()));
                purchaseUnits.add(new PayPalPurchaseUnit(amount));
            }
            request.setOrderIntent(PayPalOrderIntent.CAPTURE);
            request.setPurchaseUnits(purchaseUnits);

            try {
                PayPalOrderResponse response = service.createOrder(request);
                if(response != null){
                    order.setPaypalOrderId(response.getOrderId());
                    order.setPaypalOrderStatus(response.getStatus());
                    orderRepository.save(order);
                    return new ResponseEntity<>(response.getLinks(), HttpStatus.OK);
                } else {
                    logger.warn("YOU FUCKED UP AGAIN!");
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/checkout/success")
    public ResponseEntity<AppOrder> success(@RequestParam("token") String orderId){
        PayPalOrderCaptureResponse response = service.capturePayment(orderId);
        if(response != null){
            AppOrder order = orderRepository.findByPaypalOrderId(orderId);
            order.setPaypalOrderStatus(PayPalOrderStatus.COMPLETED);
            order.setPaypalCaptureId(response.getPurchaseUnits().get(0).getPayments().getCaptures().get(0).getId());
            order.setPaypalCaptureStatus(response.getPurchaseUnits().get(0).getPayments().getCaptures().get(0).getStatus());
            orderRepository.save(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refund/{paypal_order_id}")
    public ResponseEntity<List<PayPalLink>> refund(@PathVariable("paypal_order_id") String orderId,
                                                   @RequestBody PayPalRefundRequest request){
        try {
            logger.info("REFUND METHOD");
            PayPalRefundResponse response = service.refund(orderId, request);
            if(response != null){
                AppOrder order = orderRepository.findByPaypalOrderId(orderId);
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
