package com.atlas.order.dto;

public record OrderDto(Long id, String userEmail, Double total, String status) {}