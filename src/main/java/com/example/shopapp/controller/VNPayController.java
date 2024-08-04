package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.responses.ActionResponse;
import com.example.shopapp.responses.OrderResponse;
import com.example.shopapp.services.IOrderService;
import com.example.shopapp.services.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/vnpay")
@RequiredArgsConstructor
public class VNPayController {
    private final VNPayService vnPayService;
    private final IOrderService orderService;
    @PostMapping("/submitOrder")
    public ResponseEntity<?> submitOrder(@RequestBody OrderDTO orderDTO,
                                      HttpServletRequest request, HttpServletResponse response){
        try {
            //get total amount of product in cart base user id
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            //create order and get order id
            String vnpayUrl = vnPayService.createOrder(orderResponse.getTotalMoney(), orderResponse.getId(), baseUrl);
            ActionResponse actionResponse = new ActionResponse();
            actionResponse.setMessage(vnpayUrl);
            return ResponseEntity.ok(actionResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //parameter have user id, if payment status is success then insert new order to
        int paymentStatus =vnPayService.orderReturn(request);

        if(paymentStatus==1)
        {
            //delete product in cart
            response.sendRedirect("http://localhost:4200");
        }
        else
        {
            //delete order in database
        }
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}
