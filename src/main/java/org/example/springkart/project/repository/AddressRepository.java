package org.example.springkart.project.repository;

import org.example.springkart.project.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository <Address,Long>{
}
