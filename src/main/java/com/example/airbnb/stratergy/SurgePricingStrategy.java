package com.example.airbnb.stratergy;

import java.math.BigDecimal;

import com.example.airbnb.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class SurgePricingStrategy implements PricingStrategy {
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        return price.multiply(inventory.getSurgefactor());
    }

}
