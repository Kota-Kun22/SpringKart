package org.example.springkart.project.Controller;

import org.example.springkart.project.payload.OrderDTO;
import org.example.springkart.project.payload.OrderRequestDTO;
import org.example.springkart.project.service.OrderService;
import org.example.springkart.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @RequestBody  OrderRequestDTO orderRequestDTO){

        String emailId = authUtil.loggedInEmail();
        OrderDTO orderDto= orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);

    }
}
