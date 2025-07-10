package org.example.springkart.project.service;

import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.Address;
import org.example.springkart.project.model.User;
import org.example.springkart.project.payload.AddressDTO;
import org.example.springkart.project.repository.AddressRepository;
import org.example.springkart.project.repository.UserRepository;
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
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserRepository userRepository;

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

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));

        AddressDTO addressDto = modelMapper.map(address,AddressDTO.class);
        return addressDto;
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addressList = user.getAddresses();
        return addressList.stream()
                .map(address-> modelMapper.map(address,AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO) {

        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));

        Address address = modelMapper.map(addressDTO,Address.class);

        addressFromDb.setCity(address.getCity());
        addressFromDb.setCountry(address.getCountry());
        addressFromDb.setState(address.getState());
        addressFromDb.setStreet(address.getStreet());
        addressFromDb.setPinCode(address.getPinCode());
        addressFromDb.setBuildingName(address.getBuildingName());

        Address updatedAddress = addressRepository.save(addressFromDb);

        User user = addressFromDb.getUser();
        user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        AddressDTO updatedAddressDto = modelMapper.map(updatedAddress,AddressDTO.class);
        return updatedAddressDto;
    }
    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }
}
