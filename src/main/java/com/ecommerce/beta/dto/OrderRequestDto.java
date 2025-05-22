package com.ecommerce.beta.dto;

import java.util.UUID;

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
public class OrderRequestDto {
	
    private Float amount;
    private String currency;
    private String receipt;
    private UUID address;
    private String coupon;

}
