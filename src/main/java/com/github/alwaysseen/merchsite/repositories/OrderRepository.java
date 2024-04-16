package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<AppOrder, Integer> {
    AppOrder findByPaypalOrderId(String paypalOrderId);
    List<AppOrder> findByUser(AppUser user);
}
