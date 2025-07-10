package org.example.springkart.project.Controller;

import jakarta.validation.Valid;
import org.example.springkart.project.model.User;
import org.example.springkart.project.payload.AddressDTO;
import org.example.springkart.project.service.AddressService;
import org.example.springkart.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthUtil authUtil;


    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDto){

        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDto = addressService.createAddress(addressDto,user);
        return new ResponseEntity<>(savedAddressDto, HttpStatus.CREATED);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){

        List<AddressDTO> addressDtoList = addressService.getAddress();
        return new ResponseEntity<List<AddressDTO>>(addressDtoList,HttpStatus.OK);

    }
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){

        AddressDTO addressDto = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDto,HttpStatus.OK);

    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOList = addressService.getUserAddresses(user);
        return new ResponseEntity<List<AddressDTO>>(addressDTOList,HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(
            @PathVariable Long addressId,@RequestBody AddressDTO addressDTO){

        AddressDTO updatedAddress = addressService.updateAddressById(addressId,addressDTO);
        return new ResponseEntity<>(updatedAddress,HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> updateAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
