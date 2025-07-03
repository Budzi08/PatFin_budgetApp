package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.CategoryService;
import com.patrykb.PatFin.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Category> getUserCategories() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        return categoryService.getUserCategories(user);
    }

    @PostMapping
    public Category addCategory(@RequestBody CategoryDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        return categoryService.createCategory(dto.getName(), user);
    }
}

class CategoryDto {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
