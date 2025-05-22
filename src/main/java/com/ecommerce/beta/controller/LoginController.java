package com.ecommerce.beta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.service.ProductService;
import com.ecommerce.beta.service.UserInfoService;

import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ProductService productService;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("signedUp", false);
            return "login-page";
        }
        return "redirect:/";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam(value = "error", required = false, defaultValue = "false") Boolean error, Model model) {
        model.addAttribute("error", error);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email) {
        UserInfo user = userInfoService.findByEmail(email);
        if (user == null) {
            System.out.println("No user found with email " + email);
            return "redirect:/resetPassword?error=true";
        } else {
            String newPassword = generatePassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            userInfoService.updateUser(user);
            sendNewPassword(user.getEmail(), newPassword);
            return "redirect:/checkEmailForPassword";
        }
    }

    @GetMapping("/checkEmailForPassword")
    public String checkEmail() {
        return "checkEmailForPassword";
    }

    private void sendNewPassword(String email, String password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("msnidhilal16@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset");
        mailMessage.setText("Your new password is: " + password);
        javaMailSender.send(mailMessage);
    }

    private String generatePassword() {
        int passwordLength = 8;
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder passwordBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }
        return passwordBuilder.toString();
    }
}