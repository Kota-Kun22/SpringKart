package org.example.springkart.project.service;

import jakarta.transaction.Transactional;
import org.example.springkart.project.payload.OrderDTO;
import org.example.springkart.project.payload.OrderResponse;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId,
                        String paymentMethod, String pgName,
                        String pgPaymentId, String pgStatus,
                        String pgResponseMessage);


    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO updateOrder( Long orderId, String status);
}
