package com.ecommerce.beta.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.ecommerce.beta.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	UUID uuid;
	
    @NotNull(message = "First Name is empty")
    private String firstName;
    
    @NotNull(message = "Last Name is empty")
    private String lastName;
    
    @NotNull(message = "password is empty")
    private String password;
    
    @NotNull(message = "password is empty")
    private String newPassword;
    
    private String newPasswordRe;

    @Email(message = "not a valid email")
    private String email;

    private String phone;

    private String flat;
    private String area;
    private String town;
    private String city;
    private String state;
    private String pin;
    private String landmark;

    private List<Address> savedAddress = new ArrayList<>();
	
}
