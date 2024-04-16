package com.github.alwaysseen.merchsite.controllers;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.AppUser;
import com.github.alwaysseen.merchsite.entities.Item;
import com.github.alwaysseen.merchsite.entities.OrderItem;
import com.github.alwaysseen.merchsite.repositories.AppUserRepository;
import com.github.alwaysseen.merchsite.repositories.ItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderItemRepository;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import com.github.alwaysseen.merchsite.security.user.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository repository;
    private final OrderItemRepository orderItemRepository;
    private final AppUserRepository userRepository;
    private final ItemRepository itemRepository;
    @Autowired
    private AppUserDetailsService userDetailsService;
    @GetMapping
    public ResponseEntity<List<AppOrder>> getAll() {return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);}

    @GetMapping("/{order_id}")
    public ResponseEntity<AppOrder> getById(@PathVariable("order_id") Integer orderId){
        AppOrder order = repository.findById(orderId).get();
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    record OrderItemRequest(
           Integer itemId,
           Integer quantity
    ){}

    record OrderRequest(
            List<OrderItemRequest> orderItems
    ){}

    @PostMapping("/create")
    public ResponseEntity<AppOrder> create(@RequestBody OrderRequest request,
                                           @AuthenticationPrincipal UserDetails user){
        for(OrderItemRequest orderItemRequest: request.orderItems()){
            Item item = itemRepository.findById(orderItemRequest.itemId()).get();
            if(item.getQuantity() < orderItemRequest.quantity){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        AppOrder order = new AppOrder();
        order.setUser(userRepository.getByEmail(user.getUsername()));
        AppOrder newOrder = repository.save(order);
        for(OrderItemRequest orderItemRequest: request.orderItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setAppOrder(repository.findById(newOrder.getId()).get());
            orderItem.setItem(itemRepository.findById(orderItemRequest.itemId()).get());
            orderItem.setQuantity(orderItemRequest.quantity);
            orderItemRepository.save(orderItem);
        }
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }
}
