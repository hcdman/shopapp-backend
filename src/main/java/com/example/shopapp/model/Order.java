package com.example.shopapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "orders")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "fullname",length = 100)
    private String fullName;
    @Column(name = "email",length = 100)
    private String email;
    @Column(name = "phone_number",nullable = false,length = 100)
    private String phoneNumber;
    @Column(name = "address",length = 100)
    private String address;
    @Column(name = "note",length = 100)
    private String note;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "status")
    private String status;
    @Column(name = "total_money")
    private Float totalMoney;
    @Column(name = "shipping_method")
    private String shippingMethod;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "shipping_date")
    private LocalDate shippingDate;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "active")
    private Boolean active;

}
