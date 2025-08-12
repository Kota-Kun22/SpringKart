package org.example.springkart.project.service;

import jakarta.transaction.Transactional;
import org.example.springkart.project.exception.APIException;
import org.example.springkart.project.exception.ResourceNotFoundException;
import org.example.springkart.project.model.*;
import org.example.springkart.project.payload.OrderDTO;
import org.example.springkart.project.payload.OrderItemDTO;
import org.example.springkart.project.payload.OrderResponse;
import org.example.springkart.project.repository.*;
import org.example.springkart.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    AuthUtil authUtil;


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
        order.setOrderStatus("Accepted");
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

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder= sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Order>pageOrders= orderRepository.findAll(pageDetails);
        List<Order>orders= pageOrders.getContent();
        List<OrderDTO>orderDtos= orders.stream()
                .map(order-> modelMapper.map(order,OrderDTO.class))
                .toList();

        OrderResponse orderResponse= new OrderResponse();
        orderResponse.setContent(orderDtos);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());
        return orderResponse;
    }

    @Override
    public OrderDTO updateOrder( Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found ","orderId",orderId));
        order.setOrderStatus(status);
        orderRepository.save(order);
        return modelMapper.map(order,OrderDTO.class);
    }

    @Override
    public OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {//check it

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        User seller = authUtil.loggedInUser();

        Page<Order> pageOrders = orderRepository.findAll(pageDetails);

        List<Order> sellerOrders = pageOrders.getContent().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(orderItem -> {
                            var product = orderItem.getProduct();
                            if (product == null || product.getUser() == null) {
                                return false;
                            }
                            return product.getUser().getUserId().equals(
                                    seller.getUserId());
                        }))
                .toList();

        List<OrderDTO> orderDTOs = sellerOrders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOs);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());
        return orderResponse;
    }
}
