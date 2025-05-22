package com.ecommerce.beta.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecommerce.beta.entity.Role;
import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.repository.RoleRepository;
import com.ecommerce.beta.service.UserInfoService;
import com.ecommerce.beta.service.UserRegistrationService;
import com.ecommerce.beta.worker.UsernameProvider;

@Controller
public class AdminController {

    @Autowired
    UserRegistrationService userRegistrationService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsernameProvider usernameProvider;

    // Add admin's name to the model for all views


    // To view user
    @GetMapping("/admin/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String viewUser(@PathVariable UUID uuid, Model model) {
        UserInfo user = userInfoService.getUser(uuid);

        model.addAttribute("uuid", uuid);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("role", user.getRole());
        model.addAttribute("enabled", user.isEnabled());

        return "viewUser";
    }

    // View create user UI
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/createUser")
    public String createUser(Model model) {
        UserInfo userInfo = new UserInfo();
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("bindingResult", new BeanPropertyBindingResult(userInfo, "userInfo"));
        return "dashboard/html/user/CreateUser";
    }

    // Create user from admin dashboard
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/createUser")
    public String createNewUserFromAdminDashboard(@ModelAttribute @Valid UserInfo userInfo,
                                                  BindingResult bindingResult,
                                                  Model model) {
        if (bindingResult.hasErrors()) {
            return "dashboard/html/user/CreateUser";
        }

        // Finding the UUID of the role selected
        List<Role> roleUuids = roleRepository.findAll();
        for (Role role : roleUuids) {
            if (role.getRoleName().equals(userInfo.getRole().getRoleName())) {
                userInfo.getRole().setUuid(role.getUuid());
            }
        }

        if (!Objects.equals(userRegistrationService.addUser(userInfo), "signupSuccess")) {
            switch (userRegistrationService.addUser(userInfo)) {
                case "username" -> {
                    bindingResult.rejectValue("username", "username", "username already taken");
                }
                case "email" -> {
                    bindingResult.rejectValue("email", "email", "email address already taken");
                }
                case "phone" -> {
                    bindingResult.rejectValue("phone", "phone", "phone number already taken");
                }
                default -> {
                    System.out.println("Unusual error found");
                }
            }

            model.addAttribute("userInfo", userInfo);
            model.addAttribute("bindingResult", bindingResult);
            return "dashboard/html/user/CreateUser";
        } else {
            return "redirect:/dashboard/users/" + userInfo.getUuid();
        }
    }

    // Delete a user from dashboard
    @GetMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUserById(@PathVariable("id") UUID uuid) {
        UserInfo user = userInfoService.getUser(uuid);
        System.out.println("Hard deleting user " + user.getUsername());
        userInfoService.delete(uuid);
        return "redirect:/dashboard/users";
    }

    // Soft delete
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute UserInfo userInfo) {
        UserInfo user = userInfoService.getUser(userInfo.getUuid());
        user.setEnabled(false);
        System.out.println("Soft deleting user " + user.getUsername());
        userInfoService.save(user);
        return "redirect:/dashboard/users";
    }

    // Update User
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute UserInfo updatedUser) {
        UserInfo oldUser = userInfoService.getUser(updatedUser.getUuid());
        // Check if there is a change in role
        if (updatedUser.getRole().getRoleName().equals(oldUser.getRole().getRoleName())) {
            updatedUser.getRole().setUuid(oldUser.getRole().getUuid());
        } else {
            List<Role> allRoles = roleRepository.findAll();
            for (Role role : allRoles) {
                if (updatedUser.getRole().getRoleName().equals(role.getRoleName())) {
                    updatedUser.getRole().setUuid(role.getUuid());
                }
            }
        }
        if (updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(oldUser.getPassword());
        } else {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userInfoService.updateUser(updatedUser);
        return "redirect:/dashboard/users/" + updatedUser.getUuid();
    }
}