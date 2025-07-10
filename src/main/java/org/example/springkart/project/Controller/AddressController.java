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


}
