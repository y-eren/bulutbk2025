package com.atlas.order.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders") // "order" reserved kelime, o yüzden "orders"
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    private Double total;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    public enum Status { NEW, PAID, FAILED }

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}