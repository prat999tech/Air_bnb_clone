package com.example.airbnb.stratergy;

import java.math.BigDecimal;

import com.example.airbnb.entity.Inventory;

public class BasePriceStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBaseprice();
    }

}
