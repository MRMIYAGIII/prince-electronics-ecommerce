package com.ecommerce.beta.configuration;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.math.BigDecimal;

@ControllerAdvice(basePackages = "com.ecommerce.beta.controller.admin")
public class AdminControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BigDecimal.class, new CustomBigDecimalEditor());
    }
}