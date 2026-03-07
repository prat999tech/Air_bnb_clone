package com.example.airbnb.service;

import com.example.airbnb.entity.booking;

public interface CheckOutService {


    String getCheckOutSession(booking booking, String successUrl, String failureUrl);
}
