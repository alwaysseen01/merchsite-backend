package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.AppUser;
import com.github.alwaysseen.merchsite.entities.Item;
import com.github.alwaysseen.merchsite.entities.OrderItem;
import com.github.alwaysseen.merchsite.repositories.AppUserRepository;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository repository;
    private final AppUserRepository userRepository;
    @GetMapping
    public ResponseEntity<List<AppOrder>> getAll() {return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);}

    @GetMapping("/{order_id}")
    public ResponseEntity<AppOrder> getById(@PathVariable("order_id") Integer orderId){
        AppOrder order = repository.findById(orderId).get();
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    record OrderItemRequest(
           Integer itemId
    ){}

    record OrderRequest(
            Integer userId,
            List<OrderItemRequest> orderItems
    ){}

//    @PostMapping("/create")
//    public ResponseEntity<AppOrder> create(@RequestBody OrderRequest request){
//        AppOrder order = new AppOrder();
//        order.setUser(userRepository.findById(request.userId).get());
//        repository.save(order);
//    }
}
