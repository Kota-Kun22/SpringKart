package org.example.springkart.project.service;

import jakarta.transaction.Transactional;
import org.example.springkart.project.payload.OrderDTO;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId,
                        String paymentMethod, String pgName,
                        String pgPaymentId, String pgStatus,
                        String pgResponseMessage);




}
