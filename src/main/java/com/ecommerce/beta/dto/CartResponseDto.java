package com.ecommerce.beta.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartResponseDto {
    //order id from razorpay
    String orderId;
    //order id of the order in table
    String orderUuid;

    String razorpay_payment_id;
    String razorpay_order_id;
    String razorpay_signature;
    String razorpay_comment;
    boolean status;

    //error response
    String errorCode;
    String errorDescription;
    String errorReason;
    String errorSource;
    String errorStep;
    String paymentId;

}
