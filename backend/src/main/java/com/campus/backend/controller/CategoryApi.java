package com.campus.backend.controller;

import com.campus.backend.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryApi {

    @GetMapping("/tree")
    public Result<String> getCategoryTree() {
        return Result.success("categories", "test-tree-response");
    }

    @GetMapping
    public Result<String> getAllCategories() {
        return Result.success("categories", "test-list-response");
    }
}