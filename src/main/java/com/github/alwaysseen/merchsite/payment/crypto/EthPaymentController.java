package com.github.alwaysseen.merchsite.payment.crypto;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.EthPayment;
import com.github.alwaysseen.merchsite.entities.EthPaymentStatus;
import com.github.alwaysseen.merchsite.entities.OrderItem;
import com.github.alwaysseen.merchsite.repositories.EthPaymentRepository;
import com.github.alwaysseen.merchsite.repositories.OrderItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import com.github.alwaysseen.merchsite.security.user.AppUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("api/eth")
@RequiredArgsConstructor
@Slf4j
public class EthPaymentController {
    @Autowired
    private final EthPaymentService service;
    @Autowired
    private final EthPaymentRepository repository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;

    record PaymentRequest(
            String address
    ){};

    @PostMapping("/checkout/{order_id}")
    public ResponseEntity<AppOrder> checkout(@RequestBody PaymentRequest request,
                                             @PathVariable("order_id") int orderId,
                                             @AuthenticationPrincipal AppUserDetails user){
        AppOrder order = orderRepository.findById(orderId).get();
        if(order.getUser().getEmail().equals(user.getUsername())){
            List<OrderItem> items = orderItemRepository.findByAppOrder(order);
            double amount = 0;
            for(OrderItem item: items){
                amount += item.getQuantity()*item.getItem().getPrice();
            }
            try {
                TransactionReceipt receipt = service.capturePayment(orderId, amount, request.address);
                if(receipt.getStatus().equals("0x1")){
                    EthPayment payment = new EthPayment();
                    payment.setTxHash(receipt.getTransactionHash());
                    payment.setOrder(order);
                    order.setEthPayment(payment);
                    repository.save(payment);
                    orderRepository.save(order);
                    return new ResponseEntity<>(order, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
