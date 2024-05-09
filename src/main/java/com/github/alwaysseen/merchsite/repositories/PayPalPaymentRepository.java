package com.github.alwaysseen.merchsite.repositories;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.PayPalPayment;
import com.github.alwaysseen.merchsite.payment.paypal.response.attributes.PayPalPaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayPalPaymentRepository extends JpaRepository<PayPalPayment, Integer> {
    PayPalPayment findByPaypalOrderId(String id);
}
