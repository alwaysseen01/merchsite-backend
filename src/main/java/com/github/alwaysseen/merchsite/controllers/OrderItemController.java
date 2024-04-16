package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.OrderItem;
import com.github.alwaysseen.merchsite.repositories.OrderItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/orderitem")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemRepository repository;
    private final OrderRepository orderRepository;

    @GetMapping("/{order_id}")
    public ResponseEntity<List<OrderItem>> getByOrderId(@PathVariable("order_id") Integer orderId){
        AppOrder order = orderRepository.findById(orderId).get();
        return new ResponseEntity<>(repository.findByAppOrder(order), HttpStatus.OK);
    }
}
