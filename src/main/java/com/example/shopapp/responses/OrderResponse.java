package com.example.shopapp.responses;

import com.example.shopapp.model.OrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends BaseResponse{
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String email;
    private String note;
    @JsonProperty("order_date")
    private LocalDate orderDate;
    private String status;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_date")
    private String shippingDate;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty( "active")
    private Boolean active;
    @JsonProperty("order_details")
    private List<OrderDetail> orderDetails;
}
