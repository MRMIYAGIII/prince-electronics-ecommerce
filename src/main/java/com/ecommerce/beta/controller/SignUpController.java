package com.ecommerce.beta.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.beta.entity.Role;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.repository.RoleRepository;
import com.ecommerce.beta.repository.UserInfoRepository;
import com.ecommerce.beta.service.UserInfoService;
import com.ecommerce.beta.service.UserRegistrationService;

import javax.validation.Valid;

@Controller
@RequestMapping("/app")
public class SignUpController {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRegistrationService userRegistrationService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    public ResponseEntity<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(username);
    }

    // Landing page
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("signupError", "false");
        return "signup";
    }

    @PostMapping("/registerUser")
    public String registerUser(@Valid @ModelAttribute UserInfo userInfo, BindingResult bindingResult, Model model) {
        logger.info("Received signup request for username: {}, email: {}, phone: {}",
                userInfo.getUsername(), userInfo.getEmail(), userInfo.getPhone());

        // Validate password confirmation
        if (!userInfo.getPassword().equals(userInfo.getConfirm_password())) {
            logger.warn("Passwords do not match for username: {}", userInfo.getUsername());
            model.addAttribute("signupError", "passwordMismatch");
            return "signup";
        }

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("signupError", "invalidData");
            return "signup";
        }

        // Validate role
        String roleName = userInfo.getRole().getRoleName();
        if (roleName == null || (!roleName.equals("ROLE_USER") && !roleName.equals("ROLE_ADMIN"))) {
            logger.warn("Invalid or missing roleName: {}", roleName);
            model.addAttribute("signupError", "invalidRole");
            return "signup";
        }

        // Find or assign existing role (matching AdminController logic)
        List<Role> roles = roleRepository.findAll();
        Role selectedRole = null;
        for (Role role : roles) {
            if (role.getRoleName().equals(roleName)) {
                selectedRole = role;
                break;
            }
        }
        if (selectedRole == null) {
            selectedRole = new Role();
            selectedRole.setRoleName(roleName);
            try {
                selectedRole = roleRepository.save(selectedRole);
                logger.info("Created new role: {}", roleName);
            } catch (Exception e) {
                logger.error("Failed to create new role: {}", roleName, e);
                model.addAttribute("signupError", "roleError");
                return "signup";
            }
        }
        userInfo.setRole(selectedRole);

        try {
            String res = userRegistrationService.addUser(userInfo);
            logger.info("UserRegistrationService result: {}", res);

            if (res.equals("signupSuccess")) {
                model.addAttribute("signedUp", true);
                return "login-page";
            }

            switch (res) {
                case "phone":
                    bindingResult.rejectValue("phone", "phone", "phone number already taken");
                    break;
                case "email":
                    bindingResult.rejectValue("email", "email", "email address already taken");
                    break;
                case "username":
                    bindingResult.rejectValue("username", "username", "username already taken");
                    break;
                default:
                    model.addAttribute("signupError", "serverError"); // Reset if no specific error
                    break;
            }

            model.addAttribute("userInfo", userInfo);
            model.addAttribute("bindingResult", bindingResult);
            return "signup";
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            model.addAttribute("signupError", "serverError");
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("bindingResult", bindingResult);
            return "signup";
        }
    }
}