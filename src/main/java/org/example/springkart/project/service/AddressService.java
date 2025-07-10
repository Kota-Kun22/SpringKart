package org.example.springkart.project.service;

import org.example.springkart.project.model.User;
import org.example.springkart.project.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDto, User user);

    List<AddressDTO> getAddress();
}
