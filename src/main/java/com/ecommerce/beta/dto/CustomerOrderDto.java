package com.ecommerce.beta.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerOrderDto {
	
    private UUID uuid;

    private String date;

    private Float total;

    private Float tax;

    private Float gross;

    private Float offPrice;

    private Float pricePaid;

    private String orderStatus;

    private String orderType;
    
}
