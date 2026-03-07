package com.example.airbnb.service;

import com.example.airbnb.entity.booking;
import com.example.airbnb.entity.user;
import com.example.airbnb.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class checkoutseficeimpl implements CheckOutService {
    private final BookingRepository bookingrepository;

    @Override
    public String getCheckOutSession(booking booking, String successUrl, String failureUrl) {
        log.info("creating checkout session for booking with id: {}", booking.getId());
        user user = (user) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            CustomerCreateParams customerparams = CustomerCreateParams.builder() // here we need to pass the details of the user to create the customer in stripe and we also need to pass the email and name of the user to create the customer in stripe
                    .setEmail(user.getEmail())
                    .setName(user.getName())
                    .build();
            Customer customer = Customer.create(customerparams); // creating customer in stripe
            SessionCreateParams sessionparams = SessionCreateParams.builder() // here we need to pass the details of the booking to create the checkout session in stripe and we also need to pass the success url and failure url to redirect the user after payment is successful or failed and here we are filling details for checkout session
                    .setCustomer(customer.getId())
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1l)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // here we need to pass the amount in paise because stripe accepts the amount in paise and we also need to multiply the amount by 100 because we are passing the amount in rupees and we need to convert it to paise
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Booking for hotel: " + booking.getHotel().getName() + " and room: " + booking.getRoom().getType() + " from " + booking.getCheckInDate() + " to " + booking.getCheckOutDate())
                                                                    .setDescription("Booking ID: " + booking.getId())
                                                                    .build()
                                                    )

                                                    .build()
                                    )



                                    .build()
                    )
                    .build();
            Session session = Session.create(sessionparams); // creating checkout session in stripe
            booking.setSessionId(session.getId());
            bookingrepository.save(booking);
            log.info("creating checkout session for booking with id: {} and session id: {}", booking.getId(), session.getId());



            return session.getUrl();

        } catch (StripeException ex) {
            throw new RuntimeException(ex);
        }
        //here we need take booking instead of bookingid because we need to get the details of the booking to create the checkout session and bookingid is not enough to get the details of the booking and we need to get the details of the booking to create the checkout session and we also need to pass the success url and failure url to redirect the user after payment is successful or failed

    }
}