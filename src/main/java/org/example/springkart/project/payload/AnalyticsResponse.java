package org.example.springkart.project.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {

    private String productCount;
    private String totalRevenue;
    private String totalOrders;
}
