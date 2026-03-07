package com.example.airbnb.stratergy;

import java.math.BigDecimal;

import com.example.airbnb.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class OccupancyPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();
        if (occupancyRate > 0.8) {
            price = price.multiply(BigDecimal.valueOf(1.25));
            return price;
        } else {
            return price;
        }

    }

}
