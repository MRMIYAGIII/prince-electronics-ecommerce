package com.ecommerce.beta.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class cartDto {
	
    UUID uuid;
    int qty;
    double cartTotal = 0;
    double cartSaved = 0;
    
}
//DTO to get updated cart details