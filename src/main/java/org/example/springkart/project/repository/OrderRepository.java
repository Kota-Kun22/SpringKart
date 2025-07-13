package org.example.springkart.project.repository;

import org.example.springkart.project.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.InterfaceAddress;

@Repository
public interface OrderRepository  extends JpaRepository<Order,Long> {
}
