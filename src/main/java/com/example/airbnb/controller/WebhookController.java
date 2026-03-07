package com.example.airbnb.controller;

import com.example.airbnb.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weebhook")
@RequiredArgsConstructor
public class WebhookController {
    private final BookingService BookingService;

    @Value("${webhook.secret}")
    private String webhookSecret;

    @PostMapping("/payment")
    public ResponseEntity<Void> capturePayment(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
        try{
            Event event = Webhook.constructEvent(payload, signature, webhookSecret);
            BookingService.capturePayment(event);
            return ResponseEntity.ok().build();
        } catch(SignatureVerificationException e){
            throw new RuntimeException(e);
        }

    }
}
//TODO: i have to understand flow of this weebhook, capture payment and stripe payment gateway which i have made
//stripe will call our backend with the payload and signature in the header, we will verify the signature and then capture the payment using the payment service. We will return a 200 OK response to stripe if the payment is captured successfully, otherwise we will return a 400 Bad Request response.
// using signheader we can verify that only stripe url is calling our backend