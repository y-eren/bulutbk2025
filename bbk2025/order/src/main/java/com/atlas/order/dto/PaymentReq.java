package com.atlas.order.dto;

import java.io.Serializable;

public record PaymentReq(Long orderId, Double amount, String userEmail) implements Serializable {}
