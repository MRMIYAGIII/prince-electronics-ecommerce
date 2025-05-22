package com.ecommerce.beta.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.beta.entity.Category;
import com.ecommerce.beta.service.impl.CategoryServiceImpl;
import com.ecommerce.beta.worker.UsernameProvider;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
    @Autowired
    CategoryServiceImpl categoryService;
    
    @Autowired
    UsernameProvider usernameProvider;

    @GetMapping("create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createCategory(){
        return "dashboard/html/category/CreateCategory";
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add(@ModelAttribute Category category){
        categoryService.addCategory(category);
        return "redirect:/category/all";
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String all(Model model){
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        return "dashboard/html/category/CategoryList";
    }
    
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCategory(@PathVariable UUID uuid, Model model){
        Category category = categoryService.getCategory(uuid);

        model.addAttribute("uuid",uuid);
        model.addAttribute("name",category.getName());
        model.addAttribute("description",category.getDescription());

        return "dashboard/html/category/CategoryDetailView";
    }
    //delete a category from dashboard

    //delete


    //Update category
    @PostMapping("/updateCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String update(@ModelAttribute Category category){
        categoryService.updateCategory(category);
        return "redirect:/category/"+category.getUuid();

    }

}


