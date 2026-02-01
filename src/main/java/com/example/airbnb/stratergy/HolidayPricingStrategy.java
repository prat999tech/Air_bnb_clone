package com.example.airbnb.stratergy;

import java.math.BigDecimal;

import com.example.airbnb.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class HolidayPricingStrategy implements PricingStrategy {
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isHoliday = true;// call a third party api to check or check from local array of data
        if (isHoliday) {
            price = price.multiply(BigDecimal.valueOf(1.20));
            return price;
        } else {
            return price;
        }

    }

}
