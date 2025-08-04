package org.example.springkart.project.service;

import org.example.springkart.project.payload.AnalyticsResponse;
import org.example.springkart.project.repository.OrderRepository;
import org.example.springkart.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AnalyticsServiceImplementation  implements AnalyticsService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;


    @Override
    public AnalyticsResponse getAnalyticsData() {

        AnalyticsResponse analyticsResponse = new AnalyticsResponse();
        long productCount = productRepository.count();
        long totalOrders = orderRepository.count();
        Double totalRevenue = orderRepository.getTotalRevenue();
        analyticsResponse.setProductCount(String.valueOf(productCount));
        analyticsResponse.setTotalOrders(String.valueOf(totalOrders));
        analyticsResponse.setTotalRevenue(String.valueOf(totalRevenue == null ? 0 : totalRevenue));
        return analyticsResponse;
    }

}
