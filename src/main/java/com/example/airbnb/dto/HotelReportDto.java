package com.example.airbnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HotelReportDto {
    private Long BookingCount;
    private BigDecimal TotalRevenue;
    private BigDecimal avgRevenue;

}
