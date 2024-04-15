package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByPaypalOrderId(String paypalOrderId);
}
