package org.example.springkart.project.service;

import org.example.springkart.project.model.Address;
import org.example.springkart.project.model.User;
import org.example.springkart.project.payload.AddressDTO;
import org.example.springkart.project.repository.AddressRepository;
import org.example.springkart.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressImplementation implements AddressService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDto, User user) {

        Address address = modelMapper.map(addressDto,Address.class);
        List<Address> addressList = user.getAddresses();
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDTO savedAddressDto = modelMapper.map(savedAddress,AddressDTO.class);
        return savedAddressDto;
    }

    @Override
    public List<AddressDTO> getAddress() {

        List<Address> addressList = addressRepository.findAll();
        List<AddressDTO> addressDtos = addressList.stream()
                .map(address->modelMapper.map(address,AddressDTO.class))
                .toList();
        return addressDtos;
    }
}
