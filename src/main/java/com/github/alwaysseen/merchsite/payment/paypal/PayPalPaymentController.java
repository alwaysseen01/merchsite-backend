package com.github.alwaysseen.merchsite.payment.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.alwaysseen.merchsite.entities.*;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalOrderRequest;
import com.github.alwaysseen.merchsite.payment.paypal.request.PayPalRefundRequest;
import com.github.alwaysseen.merchsite.payment.paypal.request.attributes.*;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderCaptureResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalOrderResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.PayPalRefundResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalGetCaptureResponse;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalLink;
import com.github.alwaysseen.merchsite.repositories.ItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import com.github.alwaysseen.merchsite.repositories.PayPalPaymentRepository;
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
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final PayPalPaymentRepository paymentRepository;

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
                amount.setValue(amountValueFormat.format(orderItem.getItem().getPrice()*orderItem.getQuantity()));
                purchaseUnits.add(new PayPalPurchaseUnit(orderItem.getId().toString(), appOrderId.toString(), amount));
            }
            request.setOrderIntent(PayPalOrderIntent.CAPTURE);
            request.setPurchaseUnits(purchaseUnits);

            try {
                PayPalOrderResponse response = service.createOrder(request);
                if(response != null){
                    PayPalPayment payment = new PayPalPayment();
                    payment.setPaypalOrderId(response.getOrderId());
                    payment.setPaypalOrderStatus(response.getStatus());
                    order.setPayPalPayment(payment);
                    paymentRepository.save(payment);
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
    public ResponseEntity<List<OrderItem>> success(@RequestParam("token") String orderId){
        PayPalOrderCaptureResponse response = service.capturePayment(orderId);
        if(response != null){
            PayPalPayment payment = paymentRepository.findByPaypalOrderId(orderId);
            AppOrder order = orderRepository.findByPayPalPayment(payment);
            List<OrderItem> orderItems = orderItemRepository.findByAppOrder(order);
            for(int i=0;i<response.getPurchaseUnits().size();i++){
                for(int j=0;j<orderItems.size();j++){
                    if(orderItems.get(j).getId().toString().equals(response.getPurchaseUnits().get(i).getReferenceId())){
                        orderItems.get(j).setPayPalCaptureId(response.getPurchaseUnits().get(i).getPayments().getCaptures().get(0).getId());
                        orderItems.get(j).setPaypalCaptureStatus(response.getPurchaseUnits().get(i).getPayments().getCaptures().get(0).getStatus());
                        Item item = itemRepository.findById(orderItems.get(j).getItem().getId()).get();
                        item.setQuantity(item.getQuantity()-orderItems.get(j).getQuantity());
                        break;
                    }
                }
            }
            order.getPayPalPayment().setPaypalOrderStatus(PayPalOrderStatus.COMPLETED);
            orderRepository.save(order);
            orderItemRepository.saveAll(orderItems);
            return new ResponseEntity<>(orderItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refund/{capture_id}")
    public ResponseEntity<PayPalGetCaptureResponse> refund(@PathVariable("capture_id") String captureId,
                                                   @RequestBody PayPalRefundRequest request){
        try {
            PayPalRefundResponse refund = service.refund(captureId, request);
            if(refund != null){
                PayPalGetCaptureResponse response = service.capturedPaymentDetails(captureId);
                OrderItem orderItem = orderItemRepository.findByPayPalCaptureId(captureId);
                orderItem.setPaypalCaptureStatus(response.getStatus());
                orderItemRepository.save(orderItem);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                logger.warn("YOU FUCKED UP AGAIN!");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
