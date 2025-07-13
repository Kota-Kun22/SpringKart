package org.example.springkart.project.service;

import jakarta.transaction.Transactional;
import org.example.springkart.project.exception.APIException;
import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.*;
import org.example.springkart.project.payload.OrderDTO;
import org.example.springkart.project.payload.OrderItemDTO;
import org.example.springkart.project.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId,
                               String paymentMethod, String pgName,
                               String pgPaymentId, String pgStatus, String pgResponseMessage)
    {

        //Getting User cart
        //Create a new order with payment info
        //get items from the cart into order items

        //update product stock
        //clear the cart
        //send back the order summary


        //getting user cart
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart == null)
        {
            throw new ResourceNotFoundException("cart","email",emailId);
        }
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));

        //Create a new order with payment info here first i am going to create a new order then will create payment info obj and the we will selected to that of order

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order accepted !!! ");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId,pgStatus , pgResponseMessage,pgName);
        payment.setOrder(order);
        payment= paymentRepository.save(payment);
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        //Get items from the cart into the order items
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty())
        {
            throw new APIException("Cart is empty");
        }
        List<OrderItem>orderItems= new ArrayList<>();
        for(CartItem cartItem: cartItems)
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }
        orderItems = orderItemRepository.saveAll(orderItems);//got the

        //update product stock
        cart.getCartItems().forEach(item->{
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity()-quantity);
            productRepository.save(product);

            //clearing the cart
            cartService.deleteProductFromCart(cart.getCartId(),item.getProduct().getProductId());
        });

        //send back the order summary
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item ->
                orderDTO.getOrderItems().add(
                        modelMapper.map(item, OrderItemDTO.class)
                ));

        orderDTO.setAddressId(addressId);

        return orderDTO;
    }
}
