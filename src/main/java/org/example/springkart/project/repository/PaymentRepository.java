package org.example.springkart.project.repository;

import org.example.springkart.project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository  extends JpaRepository<Payment,Long> {
}
