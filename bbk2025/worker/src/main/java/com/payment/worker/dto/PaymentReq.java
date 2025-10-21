package com.payment.worker.dto;

public class PaymentReq {
    private Long orderId;
    private Double amount;
    private String userEmail;

    public PaymentReq() {}
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}
