package com.example.airbnb.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;

public class StripeConfig {
    public StripeConfig(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }
}
