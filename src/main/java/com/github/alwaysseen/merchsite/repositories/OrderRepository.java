package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<AppOrder, Integer> {
    AppOrder findByPaypalOrderId(String paypalOrderId);
}
