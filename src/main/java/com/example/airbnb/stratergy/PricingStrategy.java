package com.example.airbnb.stratergy;

import java.math.BigDecimal;

import com.example.airbnb.entity.Inventory;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);

}

// we are decorator pattern means we have to create multiple strategy classes
// implementing this interface
