package org.example.springkart.project.Controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.example.springkart.project.config.AppConstants;
import org.example.springkart.project.payload.*;
import org.example.springkart.project.security.services.UserDetailsImpl;
import org.example.springkart.project.service.OrderService;
import org.example.springkart.project.service.StripeService;
import org.example.springkart.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private StripeService stripeService;


    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @RequestBody  OrderRequestDTO orderRequestDTO){

        String emailId = authUtil.loggedInEmail();
        System.out.println("orderRequestDTO DATA: " + orderRequestDTO);
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

    @PostMapping("/order/stripe-client-secret")
    public ResponseEntity<String> createStripeClientSecret(@RequestBody StripePaymentDTO stripePaymentDto ) throws StripeException {

        System.out.println("stripePaymentDto Received : " + stripePaymentDto);

        PaymentIntent paymentIntent= stripeService.paymentIntent(stripePaymentDto);
        return new ResponseEntity<>(paymentIntent.getClientSecret(),HttpStatus.CREATED);
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
            @RequestParam(name="sort",defaultValue = AppConstants.SORT_BY_Amt, required = false)String sortBy,
            @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER, required = false)String sortOrder)
    {
        OrderResponse orderResponse= orderService.getAllOrders(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @PutMapping("/admin/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusDTO orderStatusDto)
    {
        OrderDTO order = orderService.updateOrder(orderId,orderStatusDto.getStatus());
        return new ResponseEntity<>(order,HttpStatus.OK);

    }

}
