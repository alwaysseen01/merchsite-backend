package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.EthPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EthPaymentRepository extends JpaRepository<EthPayment, Integer> {
}
